package inburek.interviews.quantexa.i20230419.domain

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

final class TransactionAmountTest extends AnyFreeSpecLike {
  "TransactionAmount" - {
    ".+" - {
      "should add two amounts" in {
        TransactionAmount(1.0) + TransactionAmount(2.0) shouldBe TransactionAmount(3.0)
      }
    }
  }
}
