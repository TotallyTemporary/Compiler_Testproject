package main

import codeGeneration.{CodeGenerator, IRVisitor}
import parser.Parser
import ast.*
import semanticAnalysis.{SymbolTable, SymbolicAnalyzer}
import visitors.DebugVisitor
import tokenizer.Tokenizer

object Main extends App {
  val tokens: Tokenizer = new Tokenizer("""
    sum (a b) return a+b;
  """)
  val rootNode: ProgramNode = new Parser(tokens).parse()

  SymbolicAnalyzer.visit(rootNode, null)
  println(SymbolTable.table)
  DebugVisitor.visit(rootNode, 0)
}