package itvcodingexercise.i20230413.lib

import scala.util.Random

object RandomOps {
  implicit final class RichRandom(random: Random) {
    def chooseElement[T](seq: Seq[T]): Option[T] = ???
  }
}
