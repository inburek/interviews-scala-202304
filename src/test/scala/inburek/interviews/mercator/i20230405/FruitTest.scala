package inburek.interviews.mercator.i20230405

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

final class FruitTest extends AnyFreeSpecLike {
  "Fruit" - {
    ".apply(String)" - {
      "for Apple" in {
        Fruit("Apple") shouldBe Valid(Fruit.Apple)
      }
      "for Orange" in {
        Fruit("Orange") shouldBe Valid(Fruit.Orange)
      }
      "for unknown fruit" in {
        Fruit("Banana") shouldBe Validated.invalidNel("Unknown fruit: Banana")
      }
    }
  }
}
