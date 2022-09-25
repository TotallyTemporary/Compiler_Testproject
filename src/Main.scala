package src

import codeGeneration.*
import tokenizer.*

object Main extends App {
  val tokenizer = new Tokenizer("2+2 /*") // returns 2 + 2 eof
  println(tokenizer.next())
  println(tokenizer.next())
  println(tokenizer.next())
  println(tokenizer.next())
}
