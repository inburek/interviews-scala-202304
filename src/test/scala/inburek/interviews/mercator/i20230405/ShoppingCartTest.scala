package inburek.interviews.mercator.i20230405

import cats.data.Validated
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

final class ShoppingCartTest extends AnyFreeSpecLike {
  "ShoppingCart" - {
    ".fromStrings" - {
      "for empty list" in {
        ShoppingCart.fromStrings(Nil) shouldBe Validated.validNel(ShoppingCart(Nil))
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
