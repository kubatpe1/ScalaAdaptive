package test

import grouping.LogarithmBucketSelector

import scala.util.Random

/**
  * Created by pk250187 on 3/19/17.
  */
object Test {
  val bigDataSize = 4000
  val smallDataSize = 20
  val runCount = 200
  lazy val smallData = Seq.fill(smallDataSize)(Random.nextInt).toList
  lazy val bigData = Seq.fill(bigDataSize)(Random.nextInt).toList

  def isOrdered(l : List[Int]): Boolean = (l, l.tail).zipped.forall(_ <= _)

  def runTest(): Unit = {
    Seq.range(0, runCount).foreach(i => {
      val data = if (i % 2 == 0) smallData else bigData
      println(s"Running test no: ${i} on ${data.size}")
      val res = Sorter.sort(data)
      assert(isOrdered(res))
    })
  }

  def main(args: Array[String]): Unit = {
    runTest()
  }
}