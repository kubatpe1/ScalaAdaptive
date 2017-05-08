package adaptivetests.limited

import java.time.Duration

import adaptivetests.TestRunner
import adaptivetests.testmethods.TestMethods

import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.core.runtime.Adaptive

/**
  * Created by pk250187 on 5/8/17.
  */
object LimitedTest {
  import scalaadaptive.api.Implicits._

  val testMethods = new TestMethods()
  val limitedFunc = testMethods.slowMethod _ or testMethods.fastMethod limitedTo Duration.ofSeconds(5)

  val runner = new TestRunner()

  def main(args: Array[String]): Unit = {
    Adaptive.initialize(FullHistoryTTestConfiguration)

    runner.runIncrementalTest(l => limitedFunc(l))
  }
}
