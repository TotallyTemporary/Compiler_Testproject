package src.parser

import src.parser.ast.{IntegerLiteralNode, Node, PlusNode}
import src.tokenizer.{Token, Tokenizer};

class Parser(val tokens: Tokenizer) {

  /** Begins the parsing process. Returns the root node of the abstract syntax tree. */
  def parse(): PlusNode = {
    val left: Token = tokens.next
    val operator: Token = tokens.next
    val right: Token = tokens.next

    val leftNode: IntegerLiteralNode = new IntegerLiteralNode(left.value.asInstanceOf[Integer])
    val rightNode: IntegerLiteralNode = new IntegerLiteralNode(right.value.asInstanceOf[Integer])
    val operatorNode: PlusNode = new PlusNode(leftNode, rightNode)

    operatorNode
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
                     | unaryop_expr

funccall_expr: INDENT LPAREN expr* RPAREN
  */
}
