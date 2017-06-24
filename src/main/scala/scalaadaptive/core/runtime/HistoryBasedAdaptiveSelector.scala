package scalaadaptive.core.runtime

import java.time.{Duration, Instant}

import scalaadaptive.api.grouping.{GroupId, GroupSelector}
import scalaadaptive.core.logging.Logger
import scalaadaptive.api.options.Selection
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.core.performance.{BasicPerformanceTracker, PerformanceTracker}
import scalaadaptive.core.functions.references.{FunctionReference, ReferencedFunction}
import scalaadaptive.core.runtime.history.historystorage.HistoryStorage
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.EvaluationProvider
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.invocationtokens.{InvocationTokenWithCallbacks, MeasuringInvocationToken}
import scalaadaptive.core.runtime.selection.SelectionStrategy
import scalaadaptive.core.functions.{RunData, RunResult}

/**
  * Created by pk250187 on 3/19/17.
  */
class HistoryBasedAdaptiveSelector[TMeasurement](historyStorage: HistoryStorage[TMeasurement],
                                                 discreteRunSelector: SelectionStrategy[TMeasurement],
                                                 continuousRunSelector: SelectionStrategy[TMeasurement],
                                                 measurementProvider: EvaluationProvider[TMeasurement],
                                                 bucketSelector: GroupSelector,
                                                 logger: Logger) extends AdaptiveSelector with DelayedFunctionRunner {

  private def filterHistoryByDuration(history: RunHistory[TMeasurement], duration: Duration) = {
    val currentTime = Instant.now()
    history.takeWhile(
      rd => Duration.between(rd.time, currentTime).compareTo(duration) < 0)
  }

  private def getSelectorForSelection(selection: Selection) = selection match {
    case Selection.Predictive => continuousRunSelector
    case Selection.NonPredictive => discreteRunSelector
  }

  private def selectRecord[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                  runSelector: SelectionStrategy[TMeasurement],
                                                  groupId: GroupId,
                                                  inputDescriptor: Option[Long],
                                                  limitedBy: Option[Duration]) = {
    logger.log(s"Target group: $groupId, byValue: $inputDescriptor")

    val allHistories = options.map(f => historyStorage.getHistory(HistoryKey(f.reference, groupId)))

    val histories = limitedBy match {
      case Some(duration) => allHistories.map(h => filterHistoryByDuration(h, duration))
      case _ => allHistories
    }

    val selectedRecord =
      if (histories.length > 1)
        runSelector.selectOption(histories, inputDescriptor)
      else
        histories.head

    logger.log(s"Selected option: ${selectedRecord.reference}")
    selectedRecord
  }

  override def runOption[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                arguments: TArgType,
                                                groupId: GroupId,
                                                inputDescriptor: Option[Long],
                                                limitedBy: Option[Duration],
                                                selection: Selection): RunResult[TReturnType] = {
    val tracker = new BasicPerformanceTracker
    tracker.startTracking()

    // Select function to run
    val selectedRecord = selectRecord(options, getSelectorForSelection(selection), groupId, inputDescriptor, limitedBy)
    val functionToRun = options.find(_.reference == selectedRecord.reference).get.fun

    tracker.addSelectionTime()

    // Execute the function
    runMeasuredFunction(functionToRun, arguments, selectedRecord.key, inputDescriptor, tracker)
  }

  override def runOptionWithDelayedMeasure[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                                  arguments: TArgType,
                                                                  groupId: GroupId,
                                                                  inputDescriptor: Option[Long],
                                                                  limitedBy: Option[Duration],
                                                                  selection: Selection): (TReturnType, InvocationTokenWithCallbacks) = {
    val tracker = new BasicPerformanceTracker
    tracker.startTracking()

    // Select function to run
    val selectedRecord = selectRecord(options, getSelectorForSelection(selection), groupId, inputDescriptor, limitedBy)
    val functionToRun = options.find(_.reference == selectedRecord.reference).get.fun

    tracker.addSelectionTime()

    // Execute the function
    (functionToRun(arguments), new MeasuringInvocationToken(this, inputDescriptor, selectedRecord.key, tracker))
  }

  override def runMeasuredFunction[TArgType, TReturnType](fun: (TArgType) => TReturnType,
                                                          arguments: TArgType,
                                                          key: HistoryKey,
                                                          inputDescriptor: Option[Long],
                                                          tracker: PerformanceTracker): RunResult[TReturnType] = {
    tracker.startTracking()

    // Execute the function with evaluation
    val (result, measurement) = measurementProvider.evaluateFunctionRun(fun, arguments)

    tracker.addFunctionTime()
    logger.log(s"Performance on $inputDescriptor measured: $measurement")

    // Store the evaluation result to the history
    historyStorage.applyNewRun(key, new EvaluationData[TMeasurement](inputDescriptor, Instant.now, measurement))

    tracker.addStoringTime()

    val performance = tracker.getPerformance
    performance.toLogString.lines.foreach(logger.log)
    tracker.reset()

    new RunResult(result, new RunData(key.function, key.groupId, inputDescriptor, performance))
  }

  override def flushHistory(reference: FunctionReference): Unit =
    historyStorage.flushHistory(reference)
}