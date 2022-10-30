package ast

import ast.visitors.ReturnArgVisitor

class IfNode(val condition: Expression, val statement: Statement) extends Statement {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}
