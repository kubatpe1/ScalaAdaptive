package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{CachedAverageRunHistory, ImmutableFullRunHistory}

/**
  * Created by pk250187 on 5/1/17.
  */
class ImmutableFullHistoryInterpolationConfiguration
  extends BaseLongConfiguration
    with InterpolationSelection
    with RunTimeMeasurement
    with DefaultPath
    with BufferedSerialization
    with TTestSelection
    // Temporary change:
    with NoGrouping {
  override val createHistoryStorage: () => HistoryStorage[TMeasurement] = () => {
    new MapHistoryStorage[TMeasurement](key =>
      new CachedAverageRunHistory[TMeasurement](new ImmutableFullRunHistory[TMeasurement](key)(num))(num)
    )
  }
}