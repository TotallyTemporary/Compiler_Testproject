package ast

import ast.visitors.ReturnArgVisitor

class FuncDeclNode(val name: String, val body: BlockNode, val params: ParamNode*) extends Statement {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}
