package scalaadaptive.api.policies.builder

import scalaadaptive.api.policies.{Policy, StatisticDataProvider}
import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.api.policies.builder.conditions.{Condition, SimpleCondition}

class PolicyBuilder(val inner: PolicyBuilderInner) {
  def andThen(result: PolicyResult): PolicyBuilderNeedLimit =
    new PolicyBuilderNeedLimit(inner, result)
  def andThenGoTo(policy: Policy): Policy =
    inner.addPolicy(policy).generateCyclic()
  def andThenRepeat: Policy =
    inner.generateCyclic()
  def andThenIf(condition: Condition): PolicyBuilderNeedNextPolicy =
    new PolicyBuilderNeedNextPolicy(inner, condition)
  def andThenIf(cond: (StatisticDataProvider) => Boolean): PolicyBuilderNeedNextPolicy =
    andThenIf(new SimpleCondition(cond))
}