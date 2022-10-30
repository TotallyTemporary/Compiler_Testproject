package parser

import tokenizer.Location

class ParserException(val reason: String, val location: Location) extends Exception(s"$reason at $location") {

}
