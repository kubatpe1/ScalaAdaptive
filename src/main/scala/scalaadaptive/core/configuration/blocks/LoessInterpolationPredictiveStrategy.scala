package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks.helper.{BlockWithAlpha, BlockWithLowRunLimit}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection._

/**
  * Created by pk250187 on 5/1/17.
  */
trait LoessInterpolationPredictiveStrategy extends BaseLongConfiguration
  with BlockWithLowRunLimit
  with BlockWithAlpha {
  override val createPredictiveSelectionStrategy: (Logger) => SelectionStrategy[Long] =
    (log: Logger) => new LowRunAwareSelectionStrategy[Long](
      log,
      new LeastDataSelectionStrategy[Long](log),
      new LoessInterpolationSelectionStrategy[Long](log),
      lowRunLimit)
}
