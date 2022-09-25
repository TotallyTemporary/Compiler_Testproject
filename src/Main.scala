package src

import codeGeneration.*
import tokenizer.*

object Main extends App {
  val tokenizer = new Tokenizer("2+2!=4+4")

  var token = tokenizer.next()
  while (token.tType != TokenType.END_OF_FILE) {
    println(token)
    token = tokenizer.next()
  }
}
