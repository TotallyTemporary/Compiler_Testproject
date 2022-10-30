package ast
import ast.visitors.ReturnArgVisitor

class ProgramNode(val functions: FuncDeclNode*) extends Statement {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}
