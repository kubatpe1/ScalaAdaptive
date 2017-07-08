package adaptivetests.nonpredictivetest

import tools.methods.TestMethods

import scalaadaptive.api.Adaptive
import scalaadaptive.api.options.Selection
import scalaadaptive.core.configuration.blocks.{ConsoleLogging, TTestNonPredictiveStrategy, UTestNonPredictiveStrategy}
import scalaadaptive.core.configuration.defaults.DefaultConfiguration

/**
  * Created by pk250187 on 7/8/17.
  */
object UTestCantSelect {
  def main(args: Array[String]): Unit = {
    val methods = new TestMethods

    Adaptive.initialize(new DefaultConfiguration with UTestNonPredictiveStrategy with ConsoleLogging)

    import scalaadaptive.api.Implicits._
    val function = methods.fastMethod _ or methods.anotherFastMethod or methods.slowMethod selectUsing Selection.NonPredictive

    val testRuns = 200
    Seq.range(0, testRuns).foreach(i => {
      println("Running:")
      function(List(1))
    })
  }
}
