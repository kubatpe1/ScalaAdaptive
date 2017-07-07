package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection._
import scalaadaptive.math.{BonferroniTTestRunner, MannWhitneyUTestRunner, WelchTTestRunner}

/**
  * Created by pk250187 on 5/2/17.
  */
trait UTestNonPredictiveStrategy extends BaseLongConfiguration {
  override val createNonPredictiveSelectionStrategy: (Logger) => SelectionStrategy[Long] =
    (log: Logger) => {
      val tTestRunner = new MannWhitneyUTestRunner(log)
      val leastDataSelector = new LeastDataSelectionStrategy[Long](log)
      new LowRunAwareSelectionStrategy[Long](
        log,
        leastDataSelector,
        new UTestSelectionStrategy(log, tTestRunner, leastDataSelector, 0.05),
        10)
    }
}