package configuration

import grouping.{GroupSelector, LogarithmGroupSelector}
import logging.{ConsoleLogger, FileLogger, Logger}
import performance.{PerformanceProvider, RunTimeProvider}
import runtime.history.{FullRunHistory, HistoryStorage, MapHistoryStorage, RunData}
import runtime.selection.{BestAverageSelector, LowRunAwareSelector, RunSelector}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/26/17.
  */
object TimeMeasurementAverageConfiguration extends BaseLongConfiguration {
  override val runSelector: RunSelector[MeasurementType] = new LowRunAwareSelector[Long](new BestAverageSelector(), 20)
  override val performanceProvider: PerformanceProvider[MeasurementType] = new RunTimeProvider
}
