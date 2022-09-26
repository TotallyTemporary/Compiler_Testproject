package main

import codeGeneration.CodeGenerator
import parser.Parser
import parser.ast.{AssignNode, Node}
import parser.visitors.DebugVisitor
import tokenizer.Tokenizer

object Main extends App {
  val tokens: Tokenizer = new Tokenizer("x = 3")
  val rootNode: Node = new Parser(tokens).parse()

  DebugVisitor.visit(rootNode.asInstanceOf[AssignNode], 0)
}
