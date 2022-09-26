package src.parser.visitors

import src.parser.ast.*

// A visit needs to return nothing. The second parameter holds the indentation level for cleaner debugging.
object DebugVisitor extends ReturnArgVisitor[Unit, Integer] {

  // general function.
  def visit(n: Node, arg: Integer): Unit = debug(s"Unknown node: $n", arg)


  def visit(p: PlusNode, arg: Integer): Unit =
    debug("PlusNode: ", arg)
    for (expr <- p.exprs) expr.accept(this, arg+1)

  def visit(i: IntegerLiteralNode, arg: Integer): Unit = debug(s"Integer literal: ${i.value}", arg)


  def debug(text: String, indent: Integer) = println(" "*indent+text)
}
