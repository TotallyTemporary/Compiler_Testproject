package semanticAnalysis

enum SymbolType {
  case Param, Variable
  // case Builtin
}

case class Symbol(name: String, sType: SymbolType);
