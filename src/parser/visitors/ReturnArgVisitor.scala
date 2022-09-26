package parser.visitors

import parser.ast.*

/** This type of visitor has a return type and an argument type. */
trait ReturnArgVisitor[R, A]:
  // general function.
  def visit(n: Node, arg: A): R

  def visit(a: AssignNode, arg: A): R
  def visit(d: DerefNode, arg: A): R
  def visit(d: DivNode, arg: A): R
  def visit(e: EqualsNode, arg: A): R
  def visit(i: IntegerLiteralNode, arg: A): R
  def visit(i: InverseNode, arg: A): R
  def visit(m: MulNode, arg: A): R
  def visit(p: PlusNode, arg: A): R
  def visit(r: RefNode, arg: A): R
  def visit(v: VarLiteral, arg: A): R

end ReturnArgVisitor
