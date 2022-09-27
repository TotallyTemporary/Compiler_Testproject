package parser.ast
import parser.visitors.ReturnArgVisitor

class ReturnNode(val child: Expression) extends Expression {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}
