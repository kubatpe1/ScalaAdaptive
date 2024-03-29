import adaptivetests.TestRunner
import adaptivetests.sorting.Sorter

import scalaadaptive.api.identifiers.IdentifiedFunction
import scalaadaptive.api.options.Storage

/**
  * Created by Petr Kubat on 4/29/17.
  */
object CustomIdentifierTest {
  import scalaadaptive.api.Implicits._
  val runner = new TestRunner()

  //runTest(l => l.sort())
  val sorter = new Sorter()

  val sort = sorter.selectionSort _ or sorter.standardSort by (_.size) storeUsing Storage.Persistent
  val sortWithCustom =
    IdentifiedFunction(sorter.selectionSort _, "SelectionSort") or
    IdentifiedFunction(sorter.standardSort, "StandardSort") by
      (_.size) storeUsing
      Storage.Persistent


  def main(args: Array[String]): Unit = {
    //runTest(l => sorter.sort(l))
    Seq.range(0, 1).foreach(i => {
      runner.runIncrementalTest(l => sort(l))
    })
  }
}
