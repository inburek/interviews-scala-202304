package inburek.interviews.mercator.i20230405

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

final class ShoppingCartTest extends AnyFreeSpecLike {
  "ShoppingCart" - {
    ".total" - {
      "for empty cart" in {
        ShoppingCart(List.empty).total shouldBe Pence(0)
      }
    }
  }
}
