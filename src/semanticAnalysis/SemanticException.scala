package semanticAnalysis

import tokenizer.Location

class SemanticException(val reason: String) extends Exception(reason) {

}
