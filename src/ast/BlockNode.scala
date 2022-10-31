package ast

import ast.visitors.ReturnArgVisitor

/** A block statement is a bunch of statements enclosed in braces. It should designate a new scope. */
class BlockNode(val statements: Statement*) extends Statement {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}
