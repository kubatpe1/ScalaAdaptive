package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks.helper.{BlockWithAlpha, BlockWithLowRunLimit, BlockWithWindowAverageSize}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection._
import scalaadaptive.math.RegressionConfidenceTestRunner

/**
  * Created by pk250187 on 5/2/17.
  */
trait RegressionPredictiveStrategy extends BaseLongConfiguration
  with BlockWithWindowAverageSize
  with BlockWithAlpha
  with BlockWithLowRunLimit {

  override val createPredictiveSelectionStrategy: (Logger) => SelectionStrategy[Long] =
    (log: Logger) => {
      val leastDataSelectionStrategy = new LeastDataSelectionStrategy[Long](log)
      new LowRunAwareSelectionStrategy[Long](
        log,
        leastDataSelectionStrategy,
        new RegressionSelectionStrategy[Long](log,
          new RegressionConfidenceTestRunner(log),
          leastDataSelectionStrategy,
          alpha),
        lowRunLimit)
    }
}
