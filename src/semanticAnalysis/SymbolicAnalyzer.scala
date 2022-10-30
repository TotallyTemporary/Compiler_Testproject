package semanticAnalysis

import ast.*
import ast.visitors.{DefaultVisitor, ReturnArgVisitor}

import scala.collection.mutable

object SymbolicAnalyzer extends DefaultVisitor[Unit, SymbolTable] with ReturnArgVisitor[Unit, SymbolTable] {

  /* This might violate the visitor pattern but i couldn't get the return type any other way. */
  def getFunctionSymbolTables(p: ProgramNode): mutable.Map[String, SymbolTable] = {
    // maps function name to its symbol table.
    var allSymbolTables = mutable.Map[String, SymbolTable]()

    for (func <- p.functions) {
      // make new table, populate table with params and func temp variables, then store it.
      val newTable = new SymbolTable(None)
      for (param <- func.params) do newTable.define(param.name, new Symbol(param.name))

      func.accept(this, newTable)
      allSymbolTables.put(func.name, newTable)
    }

    allSymbolTables
  }

  /* on var declaration make sure entry does not yet exist, then add one. */
  override def visit(v: VarDeclNode, arg: SymbolTable): Unit =
    super.visit(v, arg) // define var only after its expression has been evaluated (no cyclic declarations)
    arg.inThisScope(v.varName) match
      case Some(symbol) => throw new SemanticException(s"Double definition of variable ${v.varName}")
      case None => {} // cool, this variable doesn't yet exist!
    arg.define(v.varName, new Symbol(v.varName))

  /* on var literal read make sure entry exists */
  override def visit(v: VarLiteral, arg: SymbolTable): Unit =
    super.visit(v, arg)
    arg.inHierarchy(v.name) match
      case Some(symbol) => {} // cool, our variable exists!
      case None => throw new SemanticException(s"Variable ${v.name} does not exist in scope.")
}
