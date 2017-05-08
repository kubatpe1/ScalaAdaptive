package adaptivetests.closuresettings

import java.time.Duration

import adaptivetests.TestRunner
import adaptivetests.testmethods.TestMethods

import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.core.runtime.Adaptive

/**
  * Created by pk250187 on 5/8/17.
  */
object ClosureSettingsTest {
  import scalaadaptive.api.Implicits._

  val testMethods = new TestMethods()
  val closureFunc = testMethods.slowMethod _ or testMethods.fastMethod asClosures()
  val func = testMethods.slowMethod _ or testMethods.fastMethod

  val runner = new TestRunner()

  def main(args: Array[String]): Unit = {
    Adaptive.initialize(FullHistoryTTestConfiguration)

    runner.runIncrementalTest(l => closureFunc(l))
    runner.runIncrementalTest(l => func(l))
  }
}