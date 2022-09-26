package src.parser.visitors

import src.parser.ast.*

/** This type of visitor has a return type and an argument type. */
trait ReturnArgVisitor[R, A]:
  // general function.
  def visit(n: Node, arg: A): R;

  def visit(p: PlusNode, arg: A): R
  def visit(i: IntegerLiteralNode, arg: A): R

end ReturnArgVisitor
