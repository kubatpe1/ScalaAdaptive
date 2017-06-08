package adaptivetests.delayed

import java.io.PrintWriter

import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.api.options.{Selection, Storage}
import scalaadaptive.api.Adaptive

/**
  * Created by pk250187 on 4/23/17.
  */
object DelayedTest {
  import scalaadaptive.api.Implicits._
  def getFastConfig(): DelayedConfig = FastConfig()
  def getSlowConfig(): DelayedConfig = SlowConfig()
  val getConfig: () => DelayedConfig = (getFastConfig _ or getSlowConfig
    selectUsing Selection.Discrete
    storeUsing Storage.Persistent)

  def run(config: DelayedConfig) = {
    config match {
      case SlowConfig() => Thread.sleep(5)
      case FastConfig() => Thread.sleep(1)
    }
  }

  def test() = {
    val (config, measure) = getConfig^()
    for (x <- 1 to 10) {
      measure(() => run(config))
    }
  }

  def main(args: Array[String]): Unit = {
    Adaptive.initialize(new FullHistoryTTestConfiguration)

    for (x <- 1 to 20) {
      test()
    }

    getConfig.getAnalyticsData.foreach(d => d.saveData(new PrintWriter(System.out)))
  }
}
