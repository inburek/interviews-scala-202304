package inburek.interviews.quantexa.i20230419.utils

object OptionOps {
  implicit final class RichOption[A](private val option: Option[A]) extends AnyVal {
    def toEither(left: => String): Either[String, A] = {
      option.fold[Either[String, A]](Left(left))(Right(_))
    }
  }
}
