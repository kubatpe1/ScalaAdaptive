package scalaadaptive.core.functions.policies.builder.conditions

import scalaadaptive.core.functions.statistics.StatisticDataProvider

/**
  * Created by pk250187 on 6/23/17.
  */

trait Condition {
  def generate(currentStatistics: StatisticDataProvider): (StatisticDataProvider) => Boolean
  def and(condition: Condition) = new AndCondition(this, condition)
}