package adaptivetests.overhead

import adaptivetests.sorting.Sorter

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.core.configuration.blocks.NoLogger
import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.core.functions.policies.StopSelectingWhenDecidedPolicy

/**
  * Created by pk250187 on 6/5/17.
  */
object SortingOverhead {
  def customData(size: Int): List[Int] = Seq.fill(size)(Random.nextInt).toList

  val sorter = new Sorter()

  val utils = new OverheadUtils()

  def main(args: Array[String]): Unit = {
    Adaptive.initialize(new FullHistoryTTestConfiguration() with NoLogger)

    val dataSize = 1000
    val data = customData(1000)

    import scalaadaptive.api.Implicits._
    val customSort = sorter.standardSort _ or sorter.selectionSort withPolicy new StopSelectingWhenDecidedPolicy(20)

    val runCount = 1000

    utils.performTest(runCount, data, sorter.standardSort, customSort)
  }
}