package inburek.interviews.quantexa.i20230419.dataprocessing

import inburek.interviews.quantexa.i20230419.domain._
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

final class Question1Test extends AnyFreeSpecLike {
  "Question1" - {
    ".totalPerDay" - {
      def makeTransaction(day: TransactionDay, amount: TransactionAmount): Transaction = {
        Transaction(TransactionId("unused-id"), AccountId("unused-account-id"), day, TransactionCategory("unused-category"), amount)
      }

      "works on an empty collection" in {
        Question1.totalPerDay(Seq.empty) shouldBe Seq.empty
      }

      "works for one day" in {
        val input = Seq(
          makeTransaction(TransactionDay(1), TransactionAmount(1.0)),
          makeTransaction(TransactionDay(1), TransactionAmount(2.0)),
          makeTransaction(TransactionDay(1), TransactionAmount(3.0)),
        )

        Question1.totalPerDay(input) shouldBe Seq(
          TransactionTotalInADay(day = TransactionDay(1), total = TransactionAmount(6.0)),
        )
      }

      "works for multiple days provided in order" in {
        val input = Seq(
          makeTransaction(TransactionDay(1), TransactionAmount(1.0)),
          makeTransaction(TransactionDay(1), TransactionAmount(2.0)),
          makeTransaction(TransactionDay(1), TransactionAmount(3.0)),
          makeTransaction(TransactionDay(2), TransactionAmount(4.0)),
          makeTransaction(TransactionDay(2), TransactionAmount(5.0)),
          makeTransaction(TransactionDay(3), TransactionAmount(6.0)),
        )

        Question1.totalPerDay(input) shouldBe Seq(
          TransactionTotalInADay(day = TransactionDay(1), total = TransactionAmount(6.0)),
          TransactionTotalInADay(day = TransactionDay(2), total = TransactionAmount(9.0)),
          TransactionTotalInADay(day = TransactionDay(3), total = TransactionAmount(6.0)),
        )
      }
    }
  }
}
