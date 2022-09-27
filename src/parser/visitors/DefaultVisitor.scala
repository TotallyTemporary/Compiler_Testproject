package parser.visitors

import parser.ast.*

class DefaultVisitor[R, A] extends ReturnArgVisitor[R, A] {
  /* This visitor is a continuation of the ReturnArgVisitor, but it predefines all of the visit functions so they don't all have to be redefined. */

  def visit(n: Node, arg: A): R =
    n.accept(this, arg)

  def visit(a: AssignNode, arg: A): R =
    visitAllAndReturnLast(a.exprs, arg)

  def visit(b: BlockNode, arg: A): R =
    visitAllAndReturnLast(b.statements, arg)

  def visit(d: DerefNode, arg: A): R =
    d.child.accept(this, arg)

  def visit(d: DivNode, arg: A): R =
    visitAllAndReturnLast(d.exprs, arg)

  def visit(e: EqualsNode, arg: A): R =
    visitAllAndReturnLast(e.exprs, arg)

  def visit(i: IfNode, arg: A): R =
    i.condition.accept(this, arg)
    i.statement.accept(this, arg)

  def visit(i: IntegerLiteralNode, arg: A): R =
    null.asInstanceOf[R]

  def visit(i: InverseNode, arg: A): R =
    i.child.accept(this, arg)

  def visit(m: MulNode, arg: A): R =
    visitAllAndReturnLast(m.exprs, arg)

  def visit(r: ReturnNode, arg: A): R =
    r.child.accept(this, arg)

  def visit(p: PlusNode, arg: A): R =
    visitAllAndReturnLast(p.exprs, arg)

  def visit(r: RefNode, arg: A): R =
    r.child.accept(this, arg)

  def visit(v: VarLiteral, arg: A): R =
    null.asInstanceOf[R]

  def visit(v: VarDeclNode, arg: A): R =
    v.expression.accept(this, arg)

  def visit(s: StringLiteralNode, arg: A): R =
    null.asInstanceOf[R]

  def visit(w: WhileNode, arg: A): R =
    w.condition.accept(this, arg)
    w.statement.accept(this, arg)

  def visit(a: AndNode, arg: A): R =
    visitAllAndReturnLast(a.exprs, arg)

  def visit(o: OrNode, arg: A): R =
    visitAllAndReturnLast(o.exprs, arg)

  def visitAllAndReturnLast(exprs: Seq[Statement], arg: A): R = {
    for (expr <- exprs.takeRight(1)) expr.accept(this, arg)
    exprs.last.accept(this, arg)
  }


}
