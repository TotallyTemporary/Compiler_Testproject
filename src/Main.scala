package src

import codeGeneration.*
import tokenizer.*

object Main extends App {
  val tokens = new Tokenizer("2+2 /")
  tokens.foreach(token => println(token))
}
