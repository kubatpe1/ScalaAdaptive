package scalaadaptive.core.functions.identifiers

/**
  * Created by Petr Kubat on 4/29/17.
  */
trait CustomIdentifierValidator {
  def isValidIdentifier(identifier: String): Boolean
}
