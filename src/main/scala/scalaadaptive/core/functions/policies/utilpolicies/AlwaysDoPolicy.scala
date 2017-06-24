package scalaadaptive.core.functions.policies.utilpolicies

import scalaadaptive.core.functions.policies.Policy
import scalaadaptive.core.functions.policies.PolicyResult.PolicyResult
import scalaadaptive.core.functions.statistics.StatisticDataProvider

/**
  * Created by pk250187 on 6/23/17.
  *
  * A policy that will always produce given result and return itself as the next policy.
  *
  * Warning: this policy will loop forever.
  *
  */
class AlwaysDoPolicy(val result: PolicyResult) extends Policy {
  /**
    * Decides on the action to take and on the policy to use in the next decision
    *
    * @param statistics The statistics of previous runs that the decision should be based on
    * @return The result and the policy that is to be used in the next decision
    */
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) = (result, this)
}