package parser.ast

import parser.visitors.ReturnArgVisitor

class IntegerLiteralNode(var value: Integer) extends Expression {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}