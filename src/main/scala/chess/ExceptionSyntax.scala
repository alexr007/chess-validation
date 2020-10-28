package chess

object ExceptionSyntax {

  implicit class RichString(s: String) {
    def unary_! = throw new IllegalArgumentException(s)
  }

}
