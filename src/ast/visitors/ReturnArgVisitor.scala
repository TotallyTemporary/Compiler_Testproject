package ast.visitors

import ast.*

/** This type of visitor has a return type and an argument type. */
trait ReturnArgVisitor[R, A]:
  // general function.
  def visit(n: Node, arg: A): R

  def visit(a: AssignNode, arg: A): R
  def visit(b: BlockNode, arg: A): R
  def visit(d: DerefNode, arg: A): R
  def visit(d: DivNode, arg: A): R
  def visit(e: EqualsNode, arg: A): R
  def visit(i: IfNode, arg: A): R
  def visit(i: IntegerLiteralNode, arg: A): R
  def visit(i: InverseNode, arg: A): R
  def visit(m: MulNode, arg: A): R
  def visit(r: ReturnNode, arg: A): R
  def visit(p: PlusNode, arg: A): R
  def visit(r: RefNode, arg: A): R
  def visit(v: VarLiteral, arg: A): R
  def visit(v: VarDeclNode, arg: A): R
  def visit(s: StringLiteralNode, arg: A): R
  def visit(w: WhileNode, arg: A): R
  def visit(a: AndNode, arg: A): R
  def visit(o: OrNode, arg: A): R

  def visit(p: ParamNode, arg: A): R
  def visit(p: ProgramNode, arg: A): R
  def visit(f: FuncDeclNode, arg: A): R

end ReturnArgVisitor
