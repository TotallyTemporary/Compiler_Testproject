package ast

import ast.visitors.ReturnArgVisitor

class InverseNode(val child: Expression) extends Expression {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
  // inverse of x is -x.
  // this is the additive inverse.
}
