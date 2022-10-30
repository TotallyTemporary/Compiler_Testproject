package tokenizer

class Location(val row: Integer, val column: Integer):

  override def toString: String = s"row $row, column $column"

end Location
