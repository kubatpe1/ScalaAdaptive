package adaptivetests.strategycomparison

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.api.options.Selection
import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._
import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.core.functions.references.ClosureNameReference
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection.{LeastDataSelectionStrategy, LoessInterpolationSelectionStrategy, LowRunAwareSelectionStrategy, SelectionStrategy}

/**
  * Created by pk250187 on 6/24/17.
  */
object PredictiveStrategyComparison {
  val methods = new Methods

  def performTest(testData: Iterable[Int], slowerBy: Double): TestRunResult = {
    val slowerFun = methods.createSlowerQuadratic(slowerBy)
    val normalFun = methods.createNormalQuadratic

    val slowerName = slowerFun.getClass.getTypeName

    import scalaadaptive.api.Implicits._
    val fun = slowerFun or normalFun by (i => i) selectUsing Selection.Predictive

    testData.foreach(n => {
      val startTime = System.nanoTime
      val res = fun(n)
      val duration = System.nanoTime - startTime
    })

    val wrongSelected = fun.getAnalyticsData.get.getAllRunInfo.count(r => r.function match {
      case ClosureNameReference(name) => name == slowerName
      case _ => false
    })

    new TestRunResult(fun.getAnalyticsData.get, wrongSelected)
  }

  abstract class ComparisonConfiguration extends BaseLongConfiguration
    with RunTimeMeasurement
    with TTestNonPredictiveStrategy
    with CachedGroupStorage
    with DefaultHistoryPath
    with BufferedSerialization
    with NoLogging

  def main(args: Array[String]): Unit = {
    val configs = List(
      new ComparisonConfiguration
        with WindowBoundTTestPredictiveStrategy {
        override val alpha: Double = 1
      },
      new ComparisonConfiguration
        with RegressionPredictiveStrategy {
        override val alpha: Double = 0.05
      },
      new ComparisonConfiguration
        with WindowBoundRegressionPredictiveStrategy {
        override val alpha: Double = 0.05
      },
      new ComparisonConfiguration
        with WindowBoundTTestPredictiveStrategy {
        override val alpha: Double = 0.25
      },
      new ComparisonConfiguration
        with RegressionPredictiveStrategy {
        override val alpha: Double = 0.25
      },
      new ComparisonConfiguration
        with WindowBoundRegressionPredictiveStrategy {
        override val alpha: Double = 0.25
      },
      new ComparisonConfiguration
        with WindowBoundTTestPredictiveStrategy {
        override val alpha: Double = 1
      },
      new ComparisonConfiguration
        with RegressionPredictiveStrategy {
        override val alpha: Double = 1
      },
      new ComparisonConfiguration
        with WindowBoundRegressionPredictiveStrategy {
        override val alpha: Double = 1
      },
      new ComparisonConfiguration
        with LoessInterpolationPredictiveStrategy
    )

    val errorFactors = List(0.01, 0.1, 0.2, 0.5, 1.0, 3.0)

    val testCount = 5
    val runCount = 200
    val minVal = 10
    val maxVal = 500

    val inputs = Seq.fill(testCount)(Seq.fill(runCount)(Random.nextInt(maxVal - minVal) + minVal).toList)

    val resByError = errorFactors.map(err => {
      println(s"--- NEW ERROR: $err ---")
      val resByError = configs.map(cfg => {
        println("--- NEW CFG ---")
        val results = inputs.map(data => {
          Adaptive.initialize(cfg)
          val res = performTest(data, err)
          println(res.wrongSelected)
          res
        })

        results.map(_.wrongSelected)
      })
      resByError
    })

    Seq.range(0, testCount).foreach(i => {
      val lineParts = resByError.map(byErr => {
        val innerResults = byErr.map(l => l(i))
        innerResults.mkString(",")
      })
      val line = lineParts.mkString(",,")
      println(line)
    })
  }
}
