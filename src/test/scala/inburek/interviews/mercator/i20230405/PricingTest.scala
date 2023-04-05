package inburek.interviews.mercator.i20230405

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

final class PricingTest extends AnyFreeSpecLike {
  "Pricing" - {
    ".price" - {
      "Apple" in {
        Pricing.price(Fruit.Apple) shouldBe Pence(60)
      }
      "Orange" in {
        Pricing.price(Fruit.Orange) shouldBe Pence(25)
      }
    }
  }
}
