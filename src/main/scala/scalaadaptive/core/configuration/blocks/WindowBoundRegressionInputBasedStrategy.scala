package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks.helper.{BlockWithAlpha, BlockWithLowRunLimit, BlockWithWindowAverageSize}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection.support.{AverageForSampleCountProvider, FixedSizeProvider}
import scalaadaptive.core.runtime.selection._
import scalaadaptive.math.{PredictionConfidenceTestRunner, RegressionTTestRunner}

/**
  * Created by Petr Kubat on 6/7/17.
  */
trait WindowBoundRegressionInputBasedStrategy extends BaseLongConfiguration
  with BlockWithWindowAverageSize
  with BlockWithAlpha
  with BlockWithLowRunLimit {

  override val createInputBasedStrategy: (Logger) => SelectionStrategy[Long] =
    (log: Logger) => {
      val leastDataSelectionStrategy = new LeastDataSelectionStrategy[Long](log)
      new LowRunAwareSelectionStrategy[Long](
        log,
        leastDataSelectionStrategy,
        new WindowBoundSelectionStrategy[Long](log,
          new AverageForSampleCountProvider(windowAverageSize),
          new RegressionSelectionStrategy[Long](log,
            new PredictionConfidenceTestRunner(log),
            leastDataSelectionStrategy,
            createMeanBasedStrategy(log),
            alpha
          )
        ),
        lowRunLimit
      )
    }
}