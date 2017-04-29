package scalaadaptive.core.configuration
import scalaadaptive.core.grouping.GroupSelector
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.performance.PerformanceProvider
import scalaadaptive.core.runtime.history.HistoryStorage
import scalaadaptive.core.runtime.selection.RunSelector

/**
  * Created by pk250187 on 3/26/17.
  */
trait Configuration {
  type MeasurementType
  val historyStorageFactory: () => HistoryStorage[MeasurementType]
  val persistentHistoryStorageFactory: () => Option[HistoryStorage[MeasurementType]]
  val runSelector: RunSelector[MeasurementType]
  val performanceProvider: PerformanceProvider[MeasurementType]
  val groupSelector: GroupSelector
  val logger: Logger
}