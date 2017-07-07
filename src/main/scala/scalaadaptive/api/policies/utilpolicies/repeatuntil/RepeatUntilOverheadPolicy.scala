package scalaadaptive.api.policies.utilpolicies.repeatuntil

import scalaadaptive.api.policies.{Policy, StatisticDataProvider}
import scalaadaptive.api.policies.PolicyResult.PolicyResult

/**
  * Created by pk250187 on 6/1/17.
  *
  * A policy that will keep producing given result until the total overhead statistic reaches given limit.
  * Whenever this happens, it immediately lets the nextPolicy do the decision.
  *
  */
class RepeatUntilOverheadPolicy(val result: PolicyResult,
                                val overheadLimit: Long,
                                val nextPolicy: Policy) extends Policy {
  /**
    * Decides on the action to take and on the policy to use in the next decision
    *
    * @param statistics The statistics of previous runs that the decision should be based on
    * @return The result and the policy that is to be used in the next decision
    */
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) =
    if (statistics.getTotalOverheadTime < overheadLimit)
      (result, this)
    else
      nextPolicy.decide(statistics)
}