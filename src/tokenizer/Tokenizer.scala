package src.tokenizer

import scala.collection.immutable.HashMap
import scala.collection.mutable

class Tokenizer (private val input: String):
  private var index = 0

  private def currentChar = {
    if (index >= input.length) input.charAt(input.length)
    else input.charAt(index)
  }

  private def previousChar = {
    if (index == 0) input.charAt(0)
    else input.charAt(index-1)
  }

  // Possible bug? If the last character is for example "=", then we will see there be a DoubleEquals,
  // since the nextChar is duplicated. Probably not important since it's a parsing error nonetheless.
  private def nextChar = {
    if (index >= input.length-1) input.charAt(input.length)
    else input.charAt(index+1)
  }

  /** Gets the next token parsed from the input string. */
  def next(): Token = {
    // these methods must recursively call next so we don't run them in the wrong order.
    if (index >= input.length) return Token(TokenType.END_OF_FILE, null)
    if (currentChar.isWhitespace) { skipWhitespace(); return next() }
    if (skipMultilineComments()) return next()

    if (currentChar.isDigit) return getIntegerLiteral()

    var token: Token = twoCharacterTokens()
    if (token != null) return token

    token = oneCharacterTokens()
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

  private def twoCharacterTokens(): Token = {
    val tokens = HashMap(
      "=="->TokenType.DOUBLE_EQUALS, "!="->TokenType.NOT_EQUALS
    )
    val strCurr = ""+currentChar+nextChar
    val tType = tokens.get(strCurr)
    if (tType.isEmpty) return null

    advance()
    advance()
    new Token(tType.get, strCurr)
  }

  /** This function returns a token if one was found at the current index. Returns null otherwise.
   * */
  private def oneCharacterTokens(): Token = {
    val tokens = HashMap(
        '+'->TokenType.PLUS, '-'->TokenType.MINUS, '*'->TokenType.MUL, '/'->TokenType.DIV,
        '&'->TokenType.AND, '|'->TokenType.OR,
      '!'->TokenType.NOT, '='->TokenType.EQUALS,
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