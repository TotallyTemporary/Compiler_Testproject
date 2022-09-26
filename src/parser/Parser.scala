package parser

import parser.ast.*
import tokenizer.{Token, TokenType, Tokenizer}

class Parser(private val tokens: Tokenizer) {
  private var currentToken = tokens.next

  /** Begins the parsing process. Returns the root node of the abstract syntax tree. */
  def parse(): Expression = {
    expr()
  }

  /* expr: math_expr ([AND OR DOUBLE_EQUALS NOT_EQUALS EQUALS] math_expr)* | funccall_expr */
  // TODO Add funccall_expr

  private def expr(): Expression = {
    var left = math_expr()

    while (currentToken.tType == TokenType.AND || currentToken.tType == TokenType.OR
      || currentToken.tType == TokenType.DOUBLE_EQUALS || currentToken.tType == TokenType.NOT_EQUALS
      || currentToken.tType == TokenType.EQUALS) {
      val tokenType = currentToken.tType
      advance()
      left = binop(tokenType, left, math_expr())

    }

    left
  }

  /* math_expr: term ([PLUS | MINUS] term)* */
  private def math_expr(): Expression = {
    var left = term()

    while (currentToken.tType == TokenType.PLUS || currentToken.tType == TokenType.MINUS) {
      val tokenType = currentToken.tType
      advance()
      left = binop(tokenType, left, term())
    }

    left
  }

  /* term: factor ([MUL | DIV] factor)* */
  // this function is written like shit because scala doesn't have break keywords. Fuck scala.
  private def term(): Expression = {
    var left = factor()

    while (currentToken.tType == TokenType.MUL || currentToken.tType == TokenType.DIV) {
      val tokenType = currentToken.tType
      advance()
      left = binop(tokenType, left, factor())
    }

    left
  }


  /* factor: ([PLUS | MINUS | MUL | AND] factor) | (LPAREN expr RPAREN) | INTEGER_LITERAL | STRING_LITERAL | INDENT */
  private def factor(): Expression = {
    (currentToken.tType) match
      // unary tokens
      case TokenType.PLUS =>
        advance()
        factor()
      case TokenType.MINUS =>
        advance()
        new InverseNode(factor())
      case TokenType.MUL => // *x = deref(x)
        advance()
        new DerefNode(factor())
      case TokenType.AND => // &x = ref(x)
        advance()
        new RefNode(factor())

      // expression in parentheses
      case TokenType.LPAREN =>
        expect(TokenType.LPAREN)
        val inside = expr()
        expect(TokenType.RPAREN)
        inside

      case TokenType.INTEGER_LITERAL =>
        val token = currentToken
        advance()
        new IntegerLiteralNode(token.value.asInstanceOf[Integer])

      case TokenType.IDENT =>
        val token = currentToken
        advance()
        new VarLiteral(token.value.asInstanceOf[String])

      case _ => throw new ParserException(s"Expected unary operator or parentheses or literal or identifier. Got: ${currentToken.tType}")
      // TODO Add string literals
  }

  /** Move forward to the next token. Returns false if this token is actually a duplicate, last token. Returns true otherwise */
  private def advance(): Boolean = {
    val hasNext = tokens.hasNext
    this.currentToken = tokens.next
    hasNext
  }

  /** Move forward only if the current token is one of the expected types. Otherwise throw an error. */
  private def expect(types: TokenType*): Unit = {
    if (types.contains(currentToken.tType)) advance()
    else throw new ParserException(s"Expected types ${types}, got type ${currentToken.tType}")
  }

  /** Streamlines making binops a bit. PlusNode(term(), term()) = binop(PLUS, term(), term()) */
  private def binop(op: TokenType, left: Expression, right: Expression): Expression = {
    op match
      case TokenType.PLUS => PlusNode(left, right)
      case TokenType.MINUS => PlusNode(left, InverseNode(right))
      case TokenType.MUL => MulNode(left, right)
      case TokenType.DIV => DivNode(left, right)

      case TokenType.DOUBLE_EQUALS => EqualsNode(left, right)
      case TokenType.NOT_EQUALS => EqualsNode(IntegerLiteralNode(0), EqualsNode(left, right)) // x != y => (x == y) == 0
      case TokenType.EQUALS => AssignNode(left, right)

      case _ => throw new ParserException(s"$op is not a valid operation to do between two expressions.")
  }

  /*
  program: { function_declaration } EOF
function_declaration: IDENT LPAREN IDENT* RPAREN statement

statement: block_statement | if_statement | while_statement
    | return_statement | vardecl_statement | (expr SEMI) | SEMI

block_statement: LBRACKET statement* RBRACKET
if_statement: IF expr statement
while_statement: WHILE expr statement
vardecl_statement: VAR IDENT EQUALS expr SEMI
return_statement: RETURN expr SEMI

expr: math_expr ([AND OR DOUBLE_EQUALS NOT_EQUALS EQUALS] expr)*
    | funccall_expr

math_expr: term ([PLUS | MINUS] term)*

term: factor ([MUL | DIV] factor)*

factor: ([PLUS | MINUS | MUL | AND] factor) | # despite being named MUL and AND, these are the *x and &x unary reference and dereference ops.
(LPAREN expr RPAREN) | INTEGER_LITERAL | STRING_LITERAL | INDENT

funccall_expr: INDENT LPAREN expr* RPAREN
  */
}
