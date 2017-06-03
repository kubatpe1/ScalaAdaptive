package scalaadaptive.core.runtime

import scalaadaptive.core.adaptors.FunctionConfig

/**
  * Created by pk250187 on 5/27/17.
  */
trait FunctionFactory {
  def createFunction[TArgType, TRetType](firstOption: ReferencedFunction[TArgType, TRetType]): MultipleImplementationFunction[TArgType, TRetType]

  def createFunction[TArgType, TRetType](options: Seq[ReferencedFunction[TArgType, TRetType]],
                                         inputDescriptorSelector: Option[(TArgType) => Long],
                                         adaptorConfig: FunctionConfig): MultipleImplementationFunction[TArgType, TRetType]

  def createFunction[TArgType, TRetType](multipleImplementationFunction: MultipleImplementationFunction[TArgType, TRetType],
                                         adaptorConfig: FunctionConfig): MultipleImplementationFunction[TArgType, TRetType]
}
