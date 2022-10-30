package main

import codeGeneration.CodeGenerator
import parser.Parser
import parser.ast.*
import parser.visitors.DebugVisitor
import tokenizer.Tokenizer

object Main extends App {
  val tokens: Tokenizer = new Tokenizer("""
     sum (a b) {
       return a+b;
     }
  """)
  val rootNode: Node = new Parser(tokens).parse()

  DebugVisitor.visit(rootNode, 0)
}