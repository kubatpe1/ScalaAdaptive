package scalaadaptive.core.runtime.selection.strategies

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by Petr Kubat on 4/22/17.
  */
class BestExtremeSelectionStrategy(val logger: Logger) extends SelectionStrategy[Long] {
  override def selectOption(records: Seq[RunHistory[Long]], inputDescriptor: Option[Long]): HistoryKey = {
    logger.log("Selecting using BestExtremeSelector")
    records.minBy(x => x.best()).key
  }
}