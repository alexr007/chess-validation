package chess

object msg {
  def im(m: Move)(extra: String) = s"Invalid move: `$m`. $extra"
  def sc(m: Move) = s"The start cell ${m.start}"
  def tc(m: Move) = s"The target cell ${m.finish}"
  def errorParsingLocation(loc: String) = s"Error parsing location: `$loc`."
  def errorParsingMove(move: String) = s"Error parsing move: `$move`."

  def startCellIsEmpty(m: Move) = im(m)(s"${sc(m)} is empty.")
  def startCellHasWrongColor(m: Move, c: Color) = im(m)(s"${sc(m)} has the wrong color: $c, expected to be ${c.another}.")
  def invalidMoveInCheck(m: Move, c: Color) = im(m)(s"$c king is still in CHECK.")

  def targetIsNotInList(m: Move) = im(m)(s"${tc(m)} not in the list of possible moves")
  def pathIsNotClean(m: Move) = im(m)(s"Path to ${m.finish} isn't clean")
  def targetIsNotClean(m: Move) = im(m)(s"Pawn move detected. ${tc(m)} isn't clean")
  def targetIsNotOpposite(m: Move) = im(m)(s"Pawn bite detected. ${tc(m)} isn't of the opposite color")
  def targetIsNotCleanOrOpp(m: Move) = im(m)(s"${tc(m)} isn't clean or of an opposite color")

  def noKing(c: Color) = s"Weird error. There is no King of color $c"
  def fileNotFound(name: String) = s"File `$name` isn't found in the resources folder"
  val byDesign = "Shouldn't be here by design..."
  val done = "Done!"
}
