package src.tokenizer

enum TokenType:
  case IDENT // variable names, function names...
  case INTEGER_LITERAL, STRING_LITERAL // typed out numbers like 3 or 17, strings like "hello world!"
  case PLUS, MINUS, MUL, DIV, AND, OR, DOUBLE_EQUALS, EQUALS, NOT_EQUALS // binary operators: +, -, *, /, &, |, ==, =, !=
  case PLUSPLUS, MINUSMINUS, NOT // unary operators (not including +x or -x)
  case LPAREN, RPAREN, LBRACKET, RBRACKET, LBRACE, RBRACE // (, ), [, ], {, }
  case IF, WHILE, RETURN, VAR // key words
  case SEMI, COMMA, DOT // ; , .
  case END_OF_FILE // this token represents the file having ended.
end TokenType

class Token(val tType: TokenType, val value: Object):
  override def toString = s"$tType: $value"
end Token