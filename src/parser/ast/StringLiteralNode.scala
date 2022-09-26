package parser.ast

import parser.visitors.ReturnArgVisitor

class StringLiteralNode(var value: String) extends Expression {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}
