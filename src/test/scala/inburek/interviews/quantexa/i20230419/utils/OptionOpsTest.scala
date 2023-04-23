package inburek.interviews.quantexa.i20230419.utils

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

final class OptionOpsTest extends AnyFreeSpecLike {
  "OptionOps" - {
    "RichOption" - {
      import OptionOps.RichOption

      ".toEither" - {
        "works for None" in {
          None.toEither("left") shouldBe Left("left")
        }
        "works for Some" in {
          Some(1).toEither("left") shouldBe Right(1)
        }
      }
    }
  }
}
