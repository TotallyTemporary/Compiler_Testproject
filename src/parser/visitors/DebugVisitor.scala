package parser.visitors

import parser.ast.*
import parser.visitors.ReturnArgVisitor

// A visit needs to return nothing. The second parameter holds the indentation level for cleaner debugging.
object DebugVisitor extends ReturnArgVisitor[Unit, Integer] {
  def visit(n: Node, arg: Integer): Unit = debug(s"Unknown node: $n", arg)

  def visit(a: AssignNode, arg: Integer): Unit =
    debug("assign: ", arg)
    for (expr <- a.exprs) expr.accept(this, arg+1)

  def visit(d: DerefNode, arg: Integer): Unit =
    debug("deref: ", arg)
    d.child.accept(this, arg+1)

  def visit(d: DivNode, arg: Integer): Unit =
    debug("div: ", arg)
    for (expr <- d.exprs) expr.accept(this, arg+1)

  def visit(e: EqualsNode, arg: Integer): Unit =
    debug("equals: ", arg)
    for (expr <- e.exprs) expr.accept(this, arg+1)

  def visit(i: IntegerLiteralNode, arg: Integer): Unit =
    debug(s"integer literal: ${i.value}", arg)

  def visit(i: InverseNode, arg: Integer): Unit =
    debug("inverse: ", arg)
    i.child.accept(this, arg+1)

  def visit(m: MulNode, arg: Integer): Unit=
    debug("mul: ", arg)
    for (expr <- m.exprs) expr.accept(this, arg+1)

  def visit(p: PlusNode, arg: Integer): Unit =
    debug("plus: ", arg)
    for (expr <- p.exprs) expr.accept(this, arg+1)

  def visit(r: RefNode, arg: Integer): Unit =
    debug("ref: ", arg)
    r.child.accept(this, arg+1)

  def visit(v: VarLiteral, arg: Integer): Unit =
    debug(s"var literal: ${v.name}", arg)

  def visit(s: StringLiteralNode, arg: Integer): Unit =
    debug(s"string literal: ${s.value}", arg)

  def debug(str: String, indent: Integer) = println(" "*indent+str)
}