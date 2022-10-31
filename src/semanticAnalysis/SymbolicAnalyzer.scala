package semanticAnalysis

import ast.*
import ast.visitors.{DefaultVisitor, ReturnArgVisitor}

import scala.collection.mutable

object SymbolicAnalyzer extends DefaultVisitor[Unit, SymbolTable] with ReturnArgVisitor[Unit, SymbolTable] {

  override def visit(p: ProgramNode, _unused: SymbolTable): Unit = {
    for (func <- p.functions) {
      // make new scope and add the func parameters to it
      val newTable = new SymbolTable(None, func.body, func.body)
      for (param <- func.params) do newTable.define(param.name, new Symbol(param.name, SymbolType.Param))

      // populate the scope with local variables
      func.accept(this, newTable)

      // the symbol table is ready
    }
  }

  override def visit(b: BlockNode, arg: SymbolTable): Unit =
    val newTable = new SymbolTable(Some(arg), b.statements.head, b.statements.last)
    super.visit(b, newTable)

  /* on var declaration make sure entry does not yet exist, then add one. */
  override def visit(v: VarDeclNode, arg: SymbolTable): Unit =
    super.visit(v, arg) // define var only after its expression has been evaluated (no cyclic declarations)
    arg.inThisScope(v.varName) match
      case Some(symbol) => throw new SemanticException(s"Double definition of variable ${v.varName}")
      case None => {} // cool, this variable doesn't yet exist!
    arg.define(v.varName, new Symbol(v.varName, SymbolType.Variable))

  /* on var literal read make sure entry exists */
  override def visit(v: VarLiteral, arg: SymbolTable): Unit =
    super.visit(v, arg)
    arg.inHierarchy(v.name) match
      case Some(symbol) => {} // cool, our variable exists!
      case None => throw new SemanticException(s"Variable ${v.name} does not exist in scope.")
}
