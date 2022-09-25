package src.tokenizer

import scala.collection.immutable.HashMap
import scala.collection.mutable

class Tokenizer (private val input: String):
  private var index = 0
  private def currentChar =
    if (index >= input.length) input.charAt(input.length)
    else input.charAt(index)

  /** Gets the next token parsed from the input string. */
  def next(): Token = {
    // these methods must recursively call next so we don't run them in the wrong order.
    if (index >= input.length) return Token(TokenType.END_OF_FILE, null)
    if (currentChar.isWhitespace) { skipWhitespace(); return next() }
    if (skipMultilineComments()) return next()

    if (currentChar.isDigit) return getIntegerLiteral()

    var token: Token = getSimpleTokens() // this variable will be reused
    if (token != null) return token


    println("The tokenizer failed to find any tokens!")
    null
  }

  /** Gets the integer literal at the current position. Only call if current position has a digit. */
  private def getIntegerLiteral(): Token = {
    val start = index
    while (currentChar.isDigit && advance()) {}

    val intVal: Integer = Integer.parseInt(input.substring(start, index))
    new Token(TokenType.INTEGER_LITERAL, intVal)
  }

  /** Skips whitespace. Only call if there is whitespace. */
  private def skipWhitespace(): Unit = { // while it's whitespace and moving forward is successful, repeat.
    while (currentChar.isWhitespace && advance()) {}
  }

  /** if there is a comment start at the current index, we skip until the end of it. Returns true if a comment was skipped. */
  private def skipMultilineComments(): Boolean = {
    if (input.length <= index+1) return false

    if (currentChar=='/' && input.charAt(index+1) == '*') {
      advance()
      advance()

      while (advance() && !(input.charAt(index-1) == '*' && currentChar == '/')) {}
      return true
    }
    false
  }

  /** This function returns a "simple token" if one was found at the current index. Returns null otherwise.
   * A "simple token" is a token that is defined by an unique character.
   * */
  private def getSimpleTokens(): Token = {
    val tokens = HashMap(
        '+'->TokenType.PLUS, '-'->TokenType.MINUS, '*'->TokenType.MUL, '/'->TokenType.DIV,
        '&'->TokenType.AND, '|'->TokenType.OR,
        '('->TokenType.LPAREN, ')'->TokenType.RPAREN,
        '['->TokenType.LBRACKET, ']'->TokenType.RBRACKET,
        '{'->TokenType.LBRACE, '}'->TokenType.RBRACE,
        ';'->TokenType.SEMI, ','->TokenType.COMMA, '.'->TokenType.DOT,
    )

    val now: Character = currentChar
    if (tokens.contains(currentChar)) {
      advance()
      return new Token(tokens(now), now)
    }
    null
  }

  private def advance() = {
    index += 1
    if (index >= input.length) false
    else true
  }

end Tokenizer