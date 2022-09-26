package src

import codeGeneration.*
import src.parser.Parser
import src.parser.ast.{Node, PlusNode}
import src.parser.visitors.DebugVisitor
import tokenizer.*

object Main extends App {
  val tokens: Tokenizer = new Tokenizer("2+3")
  val rootNode: PlusNode = new Parser(tokens).parse()

  DebugVisitor.visit(rootNode, 0)
}
