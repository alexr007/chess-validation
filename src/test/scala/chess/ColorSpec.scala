package chess

class ColorSpec extends ASpec {

  val data = Seq(
    Black -> White,
    White -> Black
  )

  it("another color") {
    for {
      (in, out) <- data
    } in.another shouldEqual out
  }

}
