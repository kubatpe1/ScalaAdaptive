package scalaadaptive.core.runtime.policies.overhead

import scalaadaptive.core.runtime.policies.{Policy, PolicyResult}
import scalaadaptive.core.runtime.policies.PolicyResult.PolicyResult
import scalaadaptive.core.runtime.statistics.StatisticDataProvider

/**
  * Created by pk250187 on 6/1/17.
  */
class RepeatUntilResetTimePolicy(val result: PolicyResult,
                                 val nextResetTime: Long,
                                 val nextPolicy: Policy) extends Policy {
  /**
    * Decides on the action to take and on the policy to use in the next decision
    *
    * @param statistics The statistics of previous runs that the decision should be based on
    * @return The result and the policy that is to be used in the next decision
    */
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) =
    if (System.nanoTime() < nextResetTime)
      (result, this)
    else
      (result, nextPolicy)
}
