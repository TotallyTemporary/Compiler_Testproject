package parser.ast

import parser.visitors.ReturnArgVisitor

class WhileNode(val condition: Expression, val statement: Statement) extends Statement {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}
