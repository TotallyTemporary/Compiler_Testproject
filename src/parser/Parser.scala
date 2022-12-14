package parser

import ast.*
import tokenizer.{Location, Token, TokenType, Tokenizer}

import scala.collection.mutable

class Parser(private val tokens: Iterator[Token]) {
  private var currentToken = tokens.next

  /** Begins the parsing process. Returns the root node of the abstract syntax tree. */
  def parse(): ProgramNode = {
    program()
  }

  /* program: { function_declaration } EOF */
  private def program(): ProgramNode = {
    val functions = mutable.Buffer[FuncDeclNode]()
    while (currentToken.tType != TokenType.END_OF_FILE) functions.append(function_declaration())
    expect(TokenType.END_OF_FILE)

    if (functions.isEmpty) throw new ParserException("File must contain at least one function. Top level code is not supported.", new Location(0, 0))
    new ProgramNode(functions.toSeq:_*)
  }

  /* function_declaration: IDENT LPAREN IDENT* RPAREN statement */
  /*
  *
  * sum(a b) {
  *   // something
  * }
  * */
  private def function_declaration(): FuncDeclNode = {
    // function name (sum)
    val temp = currentToken.value; expect(TokenType.IDENT)
    val funcName = temp.asInstanceOf[String]

    // function params (a, b)
    val params = mutable.Buffer[ParamNode]()
    expect(TokenType.LPAREN)
    while (currentToken.tType != TokenType.RPAREN) {
      val temp = currentToken.value; expect(TokenType.IDENT)
      val paramName = temp.asInstanceOf[String]
      params.append(new ParamNode(paramName))
    }
    expect(TokenType.RPAREN)

    // function body (something)
    // function body must have a scope so if it's not already inside braces, we add "imaginary" braces
    val statementBody = statement()
    val blockBody = statementBody match
      case b: BlockNode => b
      case b => new BlockNode(statementBody)

    // put it all together
    new FuncDeclNode(funcName, blockBody, params.toSeq:_*)
  }

  /* block_statement: LBRACE statement* RBRACE */
  private def block_statement(): Statement = {
    expect(TokenType.LBRACE)
    val statements = mutable.Buffer[Statement]()
    while (currentToken.tType != TokenType.RBRACE) statements.append(statement())

    expect(TokenType.RBRACE)
    new BlockNode(statements.toSeq:_*) // don't ask me why this works. It unpacks the statements as arguments.
  }

  /* statement: block_statement | if_statement | while_statement
    | return_statement | vardecl_statement | (expr SEMI) | SEMI */
  private def statement(): Statement = {
    currentToken.tType match
      case TokenType.LBRACE => block_statement()
      case TokenType.IF => if_statement()
      case TokenType.WHILE => while_statement()
      case TokenType.RETURN => return_statement()
      case TokenType.VAR => vardecl_statement()
      case TokenType.SEMI => new NopNode()
      case _ => {
        val expression = expr()
        expect(TokenType.SEMI)
        expression
      }
  }

  /* if_statement: IF expr statement */
  private def if_statement(): Statement = {
    expect(TokenType.IF)
    val condition = expr()
    val body = statement()
    new IfNode(condition, body)
  }

  /* while_statement: WHILE expr statement */
  private def while_statement(): Statement = {
    expect(TokenType.WHILE)
    val condition = expr()
    val body = statement()
    new WhileNode(condition, body)
  }

  /* return_statement: RETURN expr SEMI */
  private def return_statement(): Statement = {
    expect(TokenType.RETURN)
    val body = expr()
    expect(TokenType.SEMI)
    new ReturnNode(body)
  }

  /* vardecl_statement: VAR IDENT EQUALS expr SEMI */
  private def vardecl_statement(): Statement = {
    expect(TokenType.VAR)
    val temp = currentToken; expect(TokenType.IDENT)
    val name = temp.value.asInstanceOf[String]
    expect(TokenType.EQUALS)
    val expression = expr()
    expect(TokenType.SEMI)

    new VarDeclNode(name, expression)
  }

  /* expr: math_expr ([AND OR DOUBLE_EQUALS NOT_EQUALS EQUALS] math_expr)* | funccall_expr */
  // TODO Add funccall_expr
  private def expr(): Expression = {
    var left = math_expr()

    while (currentToken.tType == TokenType.AND || currentToken.tType == TokenType.OR
      || currentToken.tType == TokenType.DOUBLE_EQUALS || currentToken.tType == TokenType.NOT_EQUALS
      || currentToken.tType == TokenType.EQUALS) {
      val op = currentToken
      advance()
      left = binop(op, left, math_expr())

    }

    left
  }

  /* math_expr: term ([PLUS | MINUS] term)* */
  private def math_expr(): Expression = {
    var left = term()

    while (currentToken.tType == TokenType.PLUS || currentToken.tType == TokenType.MINUS) {
      val op = currentToken
      advance()
      left = binop(op, left, term())
    }

    left
  }

  /* term: factor ([MUL | DIV] factor)* */
  // this function is written like shit because scala doesn't have break keywords. Fuck scala.
  private def term(): Expression = {
    var left = factor()

    while (currentToken.tType == TokenType.MUL || currentToken.tType == TokenType.DIV) {
      val op = currentToken
      advance()
      left = binop(op, left, factor())
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

      case TokenType.STRING_LITERAL =>
        val token = currentToken
        advance()
        new StringLiteralNode(token.value.asInstanceOf[String])

      case TokenType.IDENT =>
        val token = currentToken
        advance()
        new VarLiteral(token.value.asInstanceOf[String])

      case _ => throw new ParserException(s"Expected unaryop,(,literal,identifier, but got ${currentToken.tType}",
        currentToken.location)
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
    else throw new ParserException(s"Expected types ${types.mkString(",")}, got type ${currentToken.tType}", currentToken.location)
  }

  /** Streamlines making binops a bit. PlusNode(term(), term()) = binop(PLUS, term(), term()) */
  private def binop(opToken: Token, left: Expression, right: Expression): Expression = {
    val op = opToken.tType
    op match
      case TokenType.PLUS => PlusNode(left, right)
      case TokenType.MINUS => PlusNode(left, InverseNode(right))
      case TokenType.MUL => MulNode(left, right)
      case TokenType.DIV => DivNode(left, right)

      case TokenType.DOUBLE_EQUALS => EqualsNode(left, right)
      case TokenType.NOT_EQUALS => EqualsNode(IntegerLiteralNode(0), EqualsNode(left, right)) // x != y => (x == y) == 0
      case TokenType.EQUALS =>
        if !isLeftValue(left) then throw new ParserException(s"$left is not a value that can be assigned to", opToken.location)
        else AssignNode(left, right)

      case TokenType.AND => AndNode(left, right)
      case TokenType.OR => OrNode(left, right)

      case _ => throw new ParserException(s"$op is not a valid operation to do between two expressions", opToken.location)
  }

  /** Only some expressions can be used in the left hand side of an assign operation
   * &x = 5; #allowed
   * 6 = 5; # not allowed */
  private def isLeftValue(expr: Expression): Boolean = {
    expr match
      case e: VarLiteral => true
      case e: DerefNode => true
      case _ => false
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
