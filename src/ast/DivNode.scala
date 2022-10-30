package ast

import ast.visitors.ReturnArgVisitor

// plus node also handles minus via inverse. maybe divnode could just be multiplying by negated version?
class DivNode(val exprs: Expression*) extends Expression {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}
