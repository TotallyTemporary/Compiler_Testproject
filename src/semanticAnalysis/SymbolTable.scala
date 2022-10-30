package semanticAnalysis

import scala.collection.mutable

/* this class can later on store many more things, such as variable type and its category */
class Symbol(name: String) {}

class SymbolTable(val parent: Option[SymbolTable]) {
  val symbolMap = mutable.Map[String, Symbol]()

  def define(name: String, symbol: Symbol) = {
    symbolMap.put(name, symbol)
  }

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

}
