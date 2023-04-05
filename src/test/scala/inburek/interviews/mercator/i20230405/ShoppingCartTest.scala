package inburek.interviews.mercator.i20230405

import cats.data.{NonEmptyList, Validated, ValidatedNel}
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

final class ShoppingCartTest extends AnyFreeSpecLike {
  "ShoppingCart" - {
    ".fromStrings" - {
      "for empty list" in {
        ShoppingCart.fromStrings(Nil) shouldBe Validated.validNel(ShoppingCart(Nil))
      }
      "for multiple apples and oranges" in {
        ShoppingCart.fromStrings(List("Apple", "Orange", "Apple", "Orange", "Orange")) shouldBe
          Validated.validNel(ShoppingCart(List(Fruit.Apple, Fruit.Orange, Fruit.Apple, Fruit.Orange, Fruit.Orange)))
      }
      "for unknown fruit" in {
        ShoppingCart.fromStrings(List("Apple", "Banana", "Orange")) shouldBe
          Validated.invalidNel("Unknown fruit: Banana")
      }
      "for multiple invalid and valid fruits" in {
        val actual: ValidatedNel[String, ShoppingCart] = ShoppingCart.fromStrings(List("Apple", "Banana", "Orange", "Apple", "Banana", "Orange", "Jackfruit", "Orange"))
        val expected: ValidatedNel[String, Nothing] = Validated.invalid(NonEmptyList.of(
          "Unknown fruit: Banana",
          "Unknown fruit: Banana",
          "Unknown fruit: Jackfruit",
        ))
        ShoppingCart.fromStrings(List("Apple", "Banana", "Orange", "Apple", "Banana", "Orange", "Jackfruit", "Orange")) shouldBe
          expected
      }
    }
    ".total" - {
      "for empty cart" in {
        ShoppingCart(List.empty).total shouldBe Pence(0)
      }
      "for cart with one apple" in {
        ShoppingCart(List(Fruit.Apple)).total shouldBe Pence(60)
      }
      "for cart with one orange" in {
        ShoppingCart(List(Fruit.Orange)).total shouldBe Pence(25)
      }
      "for cart with two apples" in {
        ShoppingCart(List(Fruit.Apple, Fruit.Apple)).total shouldBe Pence(120)
      }
      "for cart with two oranges" in {
        ShoppingCart(List(Fruit.Orange, Fruit.Orange)).total shouldBe Pence(50)
      }
      "for cart with two apples and three oranges" in {
        ShoppingCart(List(Fruit.Apple, Fruit.Apple, Fruit.Orange, Fruit.Orange, Fruit.Orange)).total shouldBe Pence(120 + 75)
      }
    }
  }
}
