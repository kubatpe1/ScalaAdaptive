package scalaadaptive.core.functions.identifiers

/**
  * Created by pk250187 on 3/19/17.
  */
abstract class FunctionIdentifier

case class MethodNameIdentifier(name: String) extends FunctionIdentifier
case class ClosureIdentifier(name: String) extends FunctionIdentifier
case class CustomIdentifier(name: String) extends FunctionIdentifier
