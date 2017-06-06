package scalaadaptive.core.runtime.history.serialization

import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData

/**
  * Created by pk250187 on 4/23/17.
  */
trait RunDataSerializer[TMeasurement] {
  def serializeRunData(run: EvaluationData[TMeasurement]): String
  def deserializeRunData(string: String): Option[EvaluationData[TMeasurement]]
}