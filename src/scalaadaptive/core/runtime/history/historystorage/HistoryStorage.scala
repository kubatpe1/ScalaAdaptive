package scalaadaptive.core.runtime.history.historystorage

import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.rundata.RunData

/**
  * Created by pk250187 on 3/21/17.
  */
trait HistoryStorage[TMeasurement] {
  /** Retrieves run history from the storage for a specified key */
  def getHistory(key: HistoryKey): RunHistory[TMeasurement]

  /** Retrieves all history keys present in the storage that contain the specified function. */
  def getKeysForFunction(function: FunctionReference): Iterable[HistoryKey]

  /** Finds out whether the specified history exists in the storage */
  def hasHistory(key: HistoryKey): Boolean

  /** Applies new run to the history specified by key in the storage. Not thread-safe in general. */
  def applyNewRun(key: HistoryKey, run: RunData[TMeasurement])

  /** Removes all runs corresponding to the history key */
  def flushHistory(key: HistoryKey)

  /** Removes all runs corresponding to the function */
  def flushHistory(function: FunctionReference) = getKeysForFunction(function).foreach(flushHistory)
}
