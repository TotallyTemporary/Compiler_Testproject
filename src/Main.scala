package src

import codeGeneration.CodeGenerator;

object Main extends App {
  val cdg = CodeGenerator()
  cdg.addString("Hello testing World!")
  println(cdg.generateASM())
}
