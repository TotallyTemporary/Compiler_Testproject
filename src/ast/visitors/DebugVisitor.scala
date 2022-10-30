package ast.visitors

import ast.*
import ast.visitors.ReturnArgVisitor

// A visit needs to return nothing. The second parameter holds the indentation level for cleaner debugging.
object DebugVisitor extends ReturnArgVisitor[Unit, Integer] {
  def visit(n: Node, arg: Integer): Unit =
    println(n)
    n.accept(this, arg) // redirect unknown type

  def visit(a: AssignNode, arg: Integer): Unit =
    debug("assign: ", arg)
    for (expr <- a.exprs) expr.accept(this, arg+1)

  def visit(b: BlockNode, arg: Integer): Unit =
    debug("block: ", arg)
    for (stmt <- b.statements) stmt.accept(this, arg+1)

  def visit(d: DerefNode, arg: Integer): Unit =
    debug("deref: ", arg)
    d.child.accept(this, arg+1)

  def visit(d: DivNode, arg: Integer): Unit =
    debug("div: ", arg)
    for (expr <- d.exprs) expr.accept(this, arg+1)

  def visit(e: EqualsNode, arg: Integer): Unit =
    debug("equals: ", arg)
    for (expr <- e.exprs) expr.accept(this, arg+1)

  def visit(i: IfNode, arg: Integer): Unit =
    debug("if cond|body: ", arg)
    i.condition.accept(this, arg+1)
    i.statement.accept(this, arg+1)

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

  def visit(r: ReturnNode, arg: Integer): Unit =
    debug("return: ", arg)
    r.child.accept(this, arg+1)

  def visit(v: VarDeclNode, arg: Integer): Unit =
    debug(s"vardecl ${v.varName}:", arg)
    v.expression.accept(this, arg+1)

  def visit(v: VarLiteral, arg: Integer): Unit =
    debug(s"var literal: ${v.name}", arg)

  def visit(s: StringLiteralNode, arg: Integer): Unit =
    debug(s"string literal: ${s.value}", arg)

  def visit(w: WhileNode, arg: Integer): Unit =
    debug("while cond|body: ", arg)
    w.condition.accept(this, arg+1)
    w.statement.accept(this, arg+1)

  def visit(a: AndNode, arg: Integer): Unit =
    debug("and: ", arg)
    for (expr <- a.exprs) expr.accept(this, arg+1)

  def visit(o: OrNode, arg: Integer): Unit =
    debug("or: ", arg)
    for (expr <- o.exprs) expr.accept(this, arg+1)

  def visit(p: ast.ParamNode, arg: Integer): Unit =
    debug(s"param: ${p.name}", arg)

  def visit(p: ast.ProgramNode, arg: Integer): Unit =
    debug(s"program start:", arg)
    for (funcDecl <- p.functions) funcDecl.accept(this, arg+1)

  def visit(f: ast.FuncDeclNode, arg: Integer): Unit =
    debug(s"func decl ${f.name} params|body:", arg)
    for (param <- f.params) param.accept(this, arg+1)
    f.body.accept(this, arg+1)

  def debug(str: String, indent: Integer) = println(" "*indent+str)
}