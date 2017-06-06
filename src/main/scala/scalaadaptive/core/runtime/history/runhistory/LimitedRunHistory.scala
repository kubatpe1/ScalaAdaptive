package scalaadaptive.core.runtime.history.runhistory

import org.apache.commons.math3.stat.descriptive.{StatisticalSummary, SummaryStatistics}

import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.{EvaluationData, GroupedEvaluationData}

/**
  * Created by pk250187 on 6/5/17.
  */
class LimitedRunHistory[TMeasurement](val limit: Long,
                                      private val internalHistory: RunHistory[TMeasurement])
  extends RunHistory[TMeasurement] {

  override def applyNewRun(runResult: EvaluationData[TMeasurement]): RunHistory[TMeasurement] =
    if (internalHistory.runCount < limit)
      internalHistory.applyNewRun(runResult)
    else
      this

  // Delegations:
  override def key: HistoryKey = internalHistory.key
  override def runCount: Int = internalHistory.runCount
  override def runItems: Iterable[EvaluationData[TMeasurement]] = internalHistory.runItems
  override def best(): Option[Double] = internalHistory.best()
  override def runAveragesGroupedByDescriptor: Map[Option[Long], GroupedEvaluationData[TMeasurement]] =
    internalHistory.runAveragesGroupedByDescriptor
  override def average(): Option[Double] = internalHistory.average()
  override def takeWhile(filter: (EvaluationData[TMeasurement]) => Boolean): RunHistory[TMeasurement] =
    internalHistory.takeWhile(filter)
  override def runStatistics: StatisticalSummary = internalHistory.runStatistics
}