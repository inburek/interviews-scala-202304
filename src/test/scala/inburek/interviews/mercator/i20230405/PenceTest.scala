package inburek.interviews.mercator.i20230405

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

final class PenceTest extends AnyFreeSpecLike {
  "Pence" - {
    "+" - {
      "0 + 0" in {
        Pence(0) + Pence(0) shouldBe Pence(0)
      }
      "1 + 1" in {
        Pence(1) + Pence(1) shouldBe Pence(2)
      }
      "10 + 31" in {
        Pence(10) + Pence(31) shouldBe Pence(41)
      }
    }
  }
}
