package src.parser.ast

import src.parser.visitors.ReturnArgVisitor

class PlusNode(val exprs: Expression*) extends Expression {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}
