package scalaadaptive.core.functions.policies.utilpolicies

import scalaadaptive.core.functions.policies.Policy
import scalaadaptive.core.functions.policies.PolicyResult.PolicyResult
import scalaadaptive.core.functions.statistics.StatisticDataProvider

/**
  * Created by pk250187 on 6/24/17.
  *
  * A policy that evaluates policy1 if condition holds, policy 2 otherwise.
  *
  */
class IfPolicy(val condition: (StatisticDataProvider) => Boolean, policy1: Policy, val policy2: Policy) extends Policy {
  /**
    * Decides on the action to take and on the policy to use in the next decision
    *
    * @param statistics The statistics of previous runs that the decision should be based on
    * @return The result and the policy that is to be used in the next decision
    */
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) =
    if (condition(statistics))
      policy1.decide(statistics)
    else
      policy2.decide(statistics)
}