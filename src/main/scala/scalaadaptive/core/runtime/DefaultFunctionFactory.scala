package scalaadaptive.core.runtime

import scalaadaptive.core.adaptors.{FunctionConfig, CustomRunner}
import scalaadaptive.core.runtime.policies.{AlwaysSelectPolicy, StartPolicy}
import scalaadaptive.core.runtime.statistics.AdaptorStatistics

/**
  * Created by pk250187 on 5/27/17.
  */
class DefaultFunctionFactory extends FunctionFactory {
  override def createFunction[TArgType, TRetType](firstOption: ReferencedFunction[TArgType, TRetType]): MultipleImplementationFunction[TArgType, TRetType] =
    new MultipleImplementationFunction[TArgType, TRetType](List(firstOption),
      None,
      new CustomRunner(AdaptiveInternal.getMultiFunctionDefaults.storage),
      AdaptiveInternal.getMultiFunctionDefaults
    )

  // TODO: Empty sequence?
  override def createFunction[TArgType, TRetType](options: Seq[ReferencedFunction[TArgType, TRetType]],
                                                  inputDescriptorSelector: Option[(TArgType) => Long],
                                                  adaptorConfig: FunctionConfig): MultipleImplementationFunction[TArgType, TRetType] =
    new MultipleImplementationFunction[TArgType, TRetType](options,
      inputDescriptorSelector,
      new CustomRunner(adaptorConfig.storage),
      adaptorConfig
    )

  override def createFunction[TArgType, TRetType](function: MultipleImplementationFunction[TArgType, TRetType],
                                                  adaptorConfig: FunctionConfig): MultipleImplementationFunction[TArgType, TRetType] =
    new MultipleImplementationFunction[TArgType, TRetType](function.functions,
      function.inputDescriptorSelector,
      new CustomRunner(adaptorConfig.storage),
      adaptorConfig
    )
}
