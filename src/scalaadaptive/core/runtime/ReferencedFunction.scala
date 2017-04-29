package scalaadaptive.core.runtime

import scalaadaptive.core.references.FunctionReference

/**
  * Created by pk250187 on 3/19/17.
  */
class ReferencedFunction[TReturnType](val fun: () => TReturnType, val reference: FunctionReference)