package main

import codeGeneration.CodeGenerator
import parser.Parser
import ast.*
import semanticAnalysis.SymbolicAnalyzer
import visitors.DebugVisitor
import tokenizer.Tokenizer

object Main extends App {
  val tokens: Tokenizer = new Tokenizer("""
    sum (a b) {
      return a+b;
    }
  """)
  val rootNode: ProgramNode = new Parser(tokens).parse()

  for (table <- SymbolicAnalyzer.getFunctionSymbolTables(rootNode)) {
    println(table)
  }
  DebugVisitor.visit(rootNode, 0)
}