package ast

import ast.visitors.ReturnArgVisitor

class MulNode(val exprs: Expression*) extends Expression {
def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R = visitor.visit(this, arg)
}
