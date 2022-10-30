package ast

import ast.visitors.ReturnArgVisitor

class VarDeclNode(val varName: String, val expression: Expression) extends Statement {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}
