package main

import codeGeneration.CodeGenerator
import intermediate.{IRVisitor, IRTable}
import parser.Parser
import parser.ast.*
import parser.visitors.DebugVisitor
import tokenizer.Tokenizer

object Main extends App {
  val tokens: Tokenizer = new Tokenizer("x&y&z") // next up add and-operations
  val rootNode: Node = new Parser(tokens).parse()

  DebugVisitor.visit(rootNode, 0)
}