package parser.ast

import parser.visitors.ReturnArgVisitor

trait Node {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R
}
