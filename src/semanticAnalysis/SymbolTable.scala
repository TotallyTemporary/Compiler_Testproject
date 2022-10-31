package semanticAnalysis

import ast.Node

import scala.collection.mutable

object SymbolTable {
  val table = mutable.Map[(Node, Node), SymbolTable]()

}

class SymbolTable(val parent: Option[SymbolTable], val startNode: Node, val endNode: Node) {
  SymbolTable.table.put((startNode, endNode), this)
  val symbolMap = mutable.Map[String, Symbol]()

  def define(name: String, symbol: Symbol) = {
    symbolMap.put(name, symbol)
  }

  /** Returns Some(symbol) if that symbol exists in the nearest scope. */
  def inThisScope(name: String): Option[Symbol] = {
    symbolMap.get(name)
  }

  /** Returns Some(symbol) if that symbol exists somewhere in this table or its parent or its parent... */
  def inHierarchy(name: String): Option[Symbol] = {
    inThisScope(name) match {
      case Some(symbol) => Some(symbol)
      case None => this.parent match
        case None => None
        case Some(parent) => parent.inHierarchy(name)
    }
  }

  override def toString: String =
    symbolMap.mkString("\n  ", "\n  ", "\n")
}
