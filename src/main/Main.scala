package main

import codeGeneration.{CodeGenerator, IRVisitor}
import parser.Parser
import ast.*
import semanticAnalysis.{SymbolTable, SymbolicAnalyzer}
import visitors.DebugVisitor
import tokenizer.Tokenizer

object Main extends App {

  /*append (output c) {
      *output = c;
      *++output = 0;
      return output;
    }*/
  // funky names were to make sure registers were working optimally. (0 wasn't just a magic number thrown away)
  val tokens: Tokenizer = new Tokenizer("""
    append (autput a b d out c) {
      *out = c;
      out = out + 1;
      *out = 0;
      return out;
    }
  """)
  val rootNode: ProgramNode = new Parser(tokens).parse()

  SymbolicAnalyzer.visit(rootNode, null)
  DebugVisitor.visit(rootNode, 0)
  IRVisitor.visit(rootNode, null)
}