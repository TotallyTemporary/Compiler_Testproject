package parser.ast
import parser.visitors.ReturnArgVisitor

class ProgramNode(val functions: Statement*) extends Statement {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}
