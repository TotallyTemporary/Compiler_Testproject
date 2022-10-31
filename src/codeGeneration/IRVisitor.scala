package codeGeneration

import ast.*
import ir.*
import ast.visitors.DefaultVisitor
import semanticAnalysis.SymbolTable

// returns the "register" where value was last saved
object IRVisitor extends DefaultVisitor[Integer, CompilationContext] {
  /*
  override def visit(v: VarLiteral, arg: CompilationContext): Integer =
    // store var in new register
    val register = arg.getRegister
    val symbol = arg.inHierarchy(v.name).get.name
    println(s"  LOAD R$register, $symbol")
    register

  override def visit(r: ReturnNode, arg: CompilationContext): Integer =
    val returnRegister = r.child.accept(this, arg)
    println(s"  MOV rax, R$returnRegister")
    println(s"  DO RETURN")
    0

  override def visit(p: ProgramNode, arg: CompilationContext): Integer =
    for (func <- p.functions) do
      func.body.accept(this, SymbolTable.table.get(func))
    0

  override def visit(f: FuncDeclNode, arg: CompilationContext): Integer =
    println(s"${f.name}:")
    f.body.accept(this, arg)
    0

  override def visit(v: VarDeclNode, arg: CompilationContext): Integer =
    val register = v.expression.accept(this, arg)
    val symbol = arg.inHierarchy(v.name).get.name
    println(s"  LOAD $symbol, R$register")
    0

  override def visit(a: AssignNode, arg: CompilationContext): Integer =
    val register = a.exprs.last.accept(this, arg) // evaluate that which we assign to
    val symbol = a.exprs.head.accept(this, arg)
  */

  /*
  override def visit(a: AssignNode, arg: CompilationContext): Integer =
    val src = a.exprs.last.accept(this, arg) // eval src
*/

  // arithmetic

  override def visit(d: DerefNode, arg: CompilationContext): Integer = {
    val src = d.child.accept(this, arg)
    val dest = arg.getRegister
    arg.appendStatement(new Ref(dest, src))
    dest
  }

  override def visit(p: PlusNode, arg: CompilationContext): Integer =
    val left = p.exprs.head.accept(this, arg)
    val right = p.exprs.last.accept(this, arg)
    val dest = arg.getRegister
    arg.appendStatement(new Add(dest, left, right))

    dest

  override def visit(i: IntegerLiteralNode, arg: CompilationContext): Integer =
    val dest = arg.getRegister
    arg.appendStatement(new Init(dest, i.value, ""))
    dest

  // variables

  override def visit(v: VarLiteral, arg: CompilationContext): Integer =
    arg.getVar(v.name)

  override def visit(v: VarDeclNode, arg: CompilationContext): Integer = {
    val src = v.expression.accept(this, arg)
    arg.moveVar(v.varName, src)
    0
  }

  // double deref such as **x = 7; are not supported as of now.
  override def visit(a: AssignNode, arg: CompilationContext): Integer = {
    val src = a.exprs.last.accept(this, arg)

    val statement = a.exprs.head match
      case v: VarLiteral => {
        val dest = arg.getVar(v.name)
        Copy(dest, src)
      }
      // modifying memory access at variable - no need to move variable
      case d: DerefNode => Write(d.child.accept(this, arg), src)

    arg.appendStatement(statement)
    src
  }

  // control flow

  override def visit(b: BlockNode, arg: CompilationContext): Integer = {
    arg.enterScope(b.statements.head)
    super.visit(b, arg)
    arg.exitScope()
    0
  }

  override def visit(f: FuncDeclNode, _unused: CompilationContext): Integer = {
    val ctx = new CompilationContext(f)
    f.body.accept(this, ctx)

    var running: Boolean = true
    var node = ctx.firstStatement
    while (running) {
      println(node)
      node.next match
        case Some(n) => { node = n }
        case None => { running = false }
    }
    0
  }

  // default works.
  //override def visit(p: ProgramNode, _unused: CompilationContext): Integer = {}
}