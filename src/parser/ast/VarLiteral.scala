package parser.ast

import parser.visitors.ReturnArgVisitor

class VarLiteral(val name: String) extends Expression {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}
