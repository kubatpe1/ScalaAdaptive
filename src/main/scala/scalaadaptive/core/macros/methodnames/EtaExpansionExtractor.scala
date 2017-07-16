package scalaadaptive.core.macros.methodnames

import scala.reflect.macros.blackbox

/**
  * Created by Petr Kubat on 4/30/17.
  */
class EtaExpansionExtractor[C <: blackbox.Context](val c: C) {
  import c.universe._

  def extractStatements(block: Tree): Option[List[Tree]] = block match {
    case Block(statements, Function(_, _)) => Some(statements)
    case _ => None
  }

  def extractFunctionLiteral(block: Tree): Option[Tree] = block match {
    case Block(_, function) => Some(function)
    case _ => None
  }

  def extractFunctionBody(block: Tree): Option[Tree] = extractFunctionLiteral(block) match {
    case Some(Function(_, body)) => Some(body)
    case _ => None
  }

  def extractMethodCall(block: Tree): Option[Tree] = extractFunctionBody(block) match {
    // Warning! Case branch order is significant
    // Type arguments are applied to the method
    case Some(Apply(TypeApply(call, _), _)) => Some(call)
    case Some(Apply(Apply(TypeApply(call, _), _), _)) => Some(call)
    // Simple case - no type arguments to the method
    case Some(Apply(call, _)) => Some(call)
    case _ => None
  }

  def extractMethodTarget(block: Tree): Option[Tree] = extractMethodCall(block) match {
    case Some(Select(target, _)) => Some(target)
    case res => None
  }

  def extractMethodName(block: Tree): Option[Name] = extractMethodCall(block) match {
    case Some(Select(_, name)) => Some(name)
    case res => None
  }

//  def extractMethodTargetAndNameFromFunctionBodyTree(call: Tree): Option[(Tree, Name)] = call match {
//    case Apply(Select(target, method), _) => Some((target, method))
//    case _ => None
//  }
}