package inburek.interviews.quantexa.i20230419.dataprocessing

import inburek.interviews.quantexa.i20230419.domain._
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

final class Question2Test extends AnyFreeSpecLike {
  "Question2" - {
    ".averageTransactionsPerAccount" - {
      def makeTransaction(day: TransactionDay, accountId: AccountId, category: TransactionCategory, amount: TransactionAmount): Transaction = {
        Transaction(TransactionId("unused-id"), accountId, day, category, amount)
      }

      "works for an empty collection" in {
        Question2.averageTransactionsPerAccount(Seq.empty) shouldBe Seq.empty
      }

      "works for one day with one account" in {
        val input = Seq(
          makeTransaction(TransactionDay(1), AccountId("account-id-1"), TransactionCategory.categoryAA, TransactionAmount(1.0)),
          makeTransaction(TransactionDay(1), AccountId("account-id-1"), TransactionCategory.categoryFF, TransactionAmount(2.0)),
          makeTransaction(TransactionDay(1), AccountId("account-id-1"), TransactionCategory.categoryAA, TransactionAmount(3.0)),
        )

        Question2.averageTransactionsPerAccount(input) shouldBe Seq(
          TransactionAveragePerAccount(
            AccountId("account-id-1"),
            Map(
              TransactionCategory.categoryAA -> TransactionAmount(2.0),
              TransactionCategory.categoryFF -> TransactionAmount(2.0),
            )
          ),
        )
      }

      "works for multiple days and multiple accounts" in {
        val input = Seq(
          makeTransaction(TransactionDay(1), AccountId("account-id-1"), TransactionCategory.categoryAA, TransactionAmount(1.0)),
          makeTransaction(TransactionDay(1), AccountId("account-id-1"), TransactionCategory.categoryFF, TransactionAmount(2.0)),
          makeTransaction(TransactionDay(1), AccountId("account-id-2"), TransactionCategory.categoryFF, TransactionAmount(3.0)),
          makeTransaction(TransactionDay(1), AccountId("account-id-1"), TransactionCategory.categoryAA, TransactionAmount(4.0)),
          makeTransaction(TransactionDay(2), AccountId("account-id-2"), TransactionCategory.categoryAA, TransactionAmount(5.0)),
          makeTransaction(TransactionDay(2), AccountId("account-id-2"), TransactionCategory.categoryCC, TransactionAmount(6.0)),
          makeTransaction(TransactionDay(2), AccountId("account-id-1"), TransactionCategory.categoryCC, TransactionAmount(7.0)),
          makeTransaction(TransactionDay(2), AccountId("account-id-3"), TransactionCategory.categoryGG, TransactionAmount(8.0)),
          makeTransaction(TransactionDay(4), AccountId("account-id-3"), TransactionCategory.categoryGG, TransactionAmount(9.0)),
        )

        Question2.averageTransactionsPerAccount(input) shouldBe Seq(
          TransactionAveragePerAccount(
            AccountId("account-id-1"),
            Map(
              TransactionCategory.categoryAA -> TransactionAmount(2.5),
              TransactionCategory.categoryCC -> TransactionAmount(7.0),
              TransactionCategory.categoryFF -> TransactionAmount(2.0),
            )
          ),
          TransactionAveragePerAccount(
            AccountId("account-id-2"),
            Map(
              TransactionCategory.categoryAA -> TransactionAmount(5.0),
              TransactionCategory.categoryCC -> TransactionAmount(6.0),
              TransactionCategory.categoryFF -> TransactionAmount(3.0),
            )
          ),
          TransactionAveragePerAccount(
            AccountId("account-id-3"),
            Map(
              TransactionCategory.categoryGG -> TransactionAmount(8.5),
            )
          ),
        )
      }
    }
  }
}
