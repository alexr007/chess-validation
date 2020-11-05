package chess

import scala.util.Try

case class Chess(private val board: Board, nextC: Color, check: Option[Color] = None) {

  /** new board, new check, switched color */
  def nextMove(b: Board, check: Boolean) = {
    val next = nextC.another
    Chess(b, next, Option.when(check)(next))
  }

  def startCellIsNotEmpty(m: Move) =
    Either.cond(!board.isFreeAt(m.start),
      m,
      ImStartCellIsEmpty(m)
    )

  def startCellIsRightColor(m: Move) =
    Either.cond(board.isColorAt(m.start, nextC),
      m,
      ImStartCellHasWrongColor(m, nextC)
    )

  def validateFigureMove(m: Move) =
    board.at(m.start).get.validateMove(m, board)

  /** "check" was absent or cleared successfully */
  def wasCheckCleared(m: Move, b: Board) =
    Either.cond(Check.fold(check, color => !Check.isKingInCheck(b, color)),
      b,
      ImInvalidMoveInCheck(m, nextC)
    )

  def isNextInCheck(b: Board) =
    Check.isKingInCheck(b, nextC.another)

  def tryToMove(m: Move) =
    Right(m)
      .flatMap(startCellIsNotEmpty)
      .flatMap(startCellIsRightColor)
      .flatMap(validateFigureMove)
      .flatMap(m => board.move(m).map(b => (m, b)))
      .flatMap((wasCheckCleared _).tupled)


  def moveValidated(m: String) =
    Move.parse(m)
      .flatMap(tryToMove)
      .map(b => (b, isNextInCheck(b)))
      .fold(
        im             => (this,        Some(im)),  // same board + error
        { case (b, ch) => (nextMove(b, ch), None) } // new board, switched color, new "check"
      )

  override def toString: String = board.toString

  def isCheckMate = check.filter(!canFix(_))

  def canFix(c: Color) = canMoveKingAwayThreat(c) || canBiteThreat || canCrossThreat

  // TODO
  def canBiteThreat = true

  // TODO
  def canCrossThreat = true

  // TODO
  def canMoveKingAwayThreat(c: Color) = {
    val kingAt = board.findKingOrDie(c)
    Directions.mvKing(kingAt)
      .flatten
      .map(Move(kingAt, _))
      .flatMap(tryToMove(_).toOption)
      .nonEmpty
  }
}

object Chess {
  import fansi.{Back => BG, Color => FG}

  def initial = new Chess(Board.initial, White)

  def printLine() = println("-----------------------")

  def encolorColor(c: Color) = {
    val cs = s"  $c  "
    c match {
      case White => BG.White(FG.DarkGray(cs))
      case Black => BG.Black(FG.LightGray(cs))
    }
  }

  def beforeMove(chess: Chess, move: String) = {
    val cs = encolorColor(chess.nextC)
    val moves = FG.Blue(move)
    println(s"$cs is going to make a move: $moves")
  }

  def afterMove(chess2: Chess, invalid: Option[InvalidMove] = None) = {
    val msg = FG.Red("message:")
    println(chess2.board)
    invalid.foreach(m => println(s"$msg ${m.rep}"))
    chess2.check.foreach(c => println(encolorColor(c).toString() + FG.Red(">CHECK!<").toString))
    printLine()
  }

  /** makes ONE move and print all details */
  def makeMove(chess: Chess, move: String) = {
    beforeMove(chess, move)
    val (chess2, message) = chess.moveValidated(move)
    afterMove(chess2, message)
    chess2
  }

  /** plays WHOLE GAME from the given state */
  def play(turns: Iterator[String], ch: Chess) = turns.foldLeft(ch)(makeMove)

  /** prints initial state nd plays WHOLE FILE */
  def play(fileName: String): Unit = {
    Try(ChessIterator.resource(fileName).map(Move.fromArray))
      .map { it =>
        val ch = initial
        afterMove(ch)
        play(it, ch)
      }
      .fold(
        t => println(t.getMessage),
        _ => println(msg.done)
      )
  }
}
