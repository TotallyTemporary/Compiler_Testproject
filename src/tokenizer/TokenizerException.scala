package tokenizer

class TokenizerException(val reason: String, val location: Location) extends Exception(s"$reason $location") {

}
