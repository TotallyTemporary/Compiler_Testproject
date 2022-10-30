package ast

import ast.visitors.ReturnArgVisitor

class BlockNode(val statements: Statement*) extends Statement {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}