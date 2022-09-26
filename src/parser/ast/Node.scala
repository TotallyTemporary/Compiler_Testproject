package src.parser.ast

import src.parser.visitors.ReturnArgVisitor

trait Node {
  def accept[R, A](visitor: ReturnArgVisitor[R, A], arg: A): R;
}
