package inburek.interviews.quantexa.i20230419.dataprocessing

import inburek.interviews.quantexa.i20230419.domain.{TransactionAmount => TA, _}
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

final class Question3Test extends AnyFreeSpecLike {
  "Question3" - {
    ".movingStatistics" - {
      def makeTransaction(day: TransactionDay, accountId: AccountId, category: TransactionCategory, amount: TA): Transaction = {
        Transaction(TransactionId("unused-id"), accountId, day, category, amount)
      }

      "works for an empty collection" in {
        Question3.movingStatistics(Seq.empty) shouldBe Seq.empty
      }

      "works for one transaction" in {
        val input = Seq(
          makeTransaction(TransactionDay(3), AccountId("a"), TransactionCategory.categoryCC, TA(1.5)),
        )

        Question3.movingStatistics(input) shouldBe Seq(
          AggregateDayStatistics(TransactionDay(4), AccountId("a"), max = TA(1.5), average = TA(1.5), aaTotal = TA(0), ccTotal = TA(1.5), ffTotal = TA(0)),
        )
      }

      "works for one account and two transactions across two consecutive days" in {
        val input = Seq(
          makeTransaction(TransactionDay(3), AccountId("a"), TransactionCategory.categoryCC, TA(1.0)),
          makeTransaction(TransactionDay(4), AccountId("a"), TransactionCategory.categoryCC, TA(2.0)),
        )

        Question3.movingStatistics(input) shouldBe Seq(
          AggregateDayStatistics(TransactionDay(4), AccountId("a"), max = TA(1), average = TA(1.0), aaTotal = TA(0), ccTotal = TA(1.0), ffTotal = TA(0)),
          AggregateDayStatistics(TransactionDay(5), AccountId("a"), max = TA(2), average = TA(1.5), aaTotal = TA(0), ccTotal = TA(3.0), ffTotal = TA(0)),
        )
      }

      "works for a few days" in {
        val input = Seq(
          makeTransaction(TransactionDay(1), AccountId("a"), TransactionCategory.categoryAA, TA(1.0)),
          makeTransaction(TransactionDay(1), AccountId("a"), TransactionCategory.categoryCC, TA(2.0)),
          makeTransaction(TransactionDay(1), AccountId("a"), TransactionCategory.categoryFF, TA(3.0)),
          makeTransaction(TransactionDay(1), AccountId("b"), TransactionCategory.categoryAA, TA(4.0)),
          makeTransaction(TransactionDay(1), AccountId("b"), TransactionCategory.categoryCC, TA(5.0)),
          makeTransaction(TransactionDay(1), AccountId("b"), TransactionCategory.categoryFF, TA(6.0)),

          makeTransaction(TransactionDay(2), AccountId("a"), TransactionCategory.categoryAA, TA(2.0)),
          makeTransaction(TransactionDay(2), AccountId("a"), TransactionCategory.categoryCC, TA(3.0)),
          makeTransaction(TransactionDay(2), AccountId("a"), TransactionCategory.categoryFF, TA(4.0)),
          makeTransaction(TransactionDay(2), AccountId("b"), TransactionCategory.categoryAA, TA(5.0)),
          makeTransaction(TransactionDay(2), AccountId("b"), TransactionCategory.categoryCC, TA(6.0)),
          makeTransaction(TransactionDay(2), AccountId("b"), TransactionCategory.categoryFF, TA(1.0)),
          makeTransaction(TransactionDay(2), AccountId("c"), TransactionCategory.categoryFF, TA(1.0)),

          makeTransaction(TransactionDay(3), AccountId("a"), TransactionCategory.categoryAA, TA(3.0)),
          makeTransaction(TransactionDay(3), AccountId("a"), TransactionCategory.categoryCC, TA(4.0)),
          makeTransaction(TransactionDay(3), AccountId("a"), TransactionCategory.categoryFF, TA(5.0)),
          makeTransaction(TransactionDay(3), AccountId("b"), TransactionCategory.categoryAA, TA(6.0)),
          makeTransaction(TransactionDay(3), AccountId("b"), TransactionCategory.categoryCC, TA(1.0)),
          makeTransaction(TransactionDay(3), AccountId("b"), TransactionCategory.categoryFF, TA(2.0)),

          makeTransaction(TransactionDay(4), AccountId("a"), TransactionCategory.categoryAA, TA(3.0)),
          makeTransaction(TransactionDay(4), AccountId("a"), TransactionCategory.categoryCC, TA(4.0)),
          makeTransaction(TransactionDay(4), AccountId("a"), TransactionCategory.categoryFF, TA(5.0)),

          makeTransaction(TransactionDay(5), AccountId("b"), TransactionCategory.categoryAA, TA(6.0)),
          makeTransaction(TransactionDay(5), AccountId("b"), TransactionCategory.categoryCC, TA(7.0)),
          makeTransaction(TransactionDay(5), AccountId("b"), TransactionCategory.categoryFF, TA(8.0)),

          makeTransaction(TransactionDay(7), AccountId("c"), TransactionCategory.categoryAA, TA(3.0)),
          makeTransaction(TransactionDay(7), AccountId("c"), TransactionCategory.categoryAA, TA(2.0)),
        )

        val actual = Question3.movingStatistics(input)
        val rounded = actual.map(old => old.copy(average = TA((old.average.value * 100).round / 100.0)))

        rounded shouldBe Seq(
          AggregateDayStatistics(TransactionDay(2), AccountId("a"), max = TA(3.0), average = TA(2.0)  , aaTotal = TA(1.0)  , ccTotal = TA(2.0)  , ffTotal = TA(3.0)),
          AggregateDayStatistics(TransactionDay(2), AccountId("b"), max = TA(6.0), average = TA(5.0)  , aaTotal = TA(4.0)  , ccTotal = TA(5.0)  , ffTotal = TA(6.0)),

          AggregateDayStatistics(TransactionDay(3), AccountId("a"), max = TA(4.0), average = TA(2.5)  , aaTotal = TA(3.0)  , ccTotal = TA(5.0)  , ffTotal = TA(7.0)),
          AggregateDayStatistics(TransactionDay(3), AccountId("b"), max = TA(6.0), average = TA(4.5)  , aaTotal = TA(9.0)  , ccTotal = TA(11.0) , ffTotal = TA(7.0)),
          AggregateDayStatistics(TransactionDay(3), AccountId("c"), max = TA(1.0), average = TA(1.0)  , aaTotal = TA(0.0)  , ccTotal = TA(0.0)  , ffTotal = TA(1.0)),

          AggregateDayStatistics(TransactionDay(4), AccountId("a"), max = TA(5.0), average = TA(3.0)  , aaTotal = TA(6.0)  , ccTotal = TA(9.0)  , ffTotal = TA(12.0)),
          AggregateDayStatistics(TransactionDay(4), AccountId("b"), max = TA(6.0), average = TA(4.0)  , aaTotal = TA(15.0) , ccTotal = TA(12.0) , ffTotal = TA(9.0)),
          AggregateDayStatistics(TransactionDay(4), AccountId("c"), max = TA(1.0), average = TA(1.0)  , aaTotal = TA(0.0)  , ccTotal = TA(0.0)  , ffTotal = TA(1.0)),

          AggregateDayStatistics(TransactionDay(5), AccountId("a"), max = TA(5.0), average = TA(3.25) , aaTotal = TA(9.0)  , ccTotal = TA(13.0) , ffTotal = TA(17.0)),
          AggregateDayStatistics(TransactionDay(5), AccountId("b"), max = TA(6.0), average = TA(4.0)  , aaTotal = TA(15.0) , ccTotal = TA(12.0) , ffTotal = TA(9.0)),
          AggregateDayStatistics(TransactionDay(5), AccountId("c"), max = TA(1.0), average = TA(1.0)  , aaTotal = TA(0.0)  , ccTotal = TA(0.0)  , ffTotal = TA(1.0)),

          AggregateDayStatistics(TransactionDay(6), AccountId("a"), max = TA(5.0), average = TA(3.25) , aaTotal = TA(9.0)  , ccTotal = TA(13.0) , ffTotal = TA(17.0)),
          AggregateDayStatistics(TransactionDay(6), AccountId("b"), max = TA(8.0), average = TA(4.75) , aaTotal = TA(21.0) , ccTotal = TA(19.0) , ffTotal = TA(17.0)),
          AggregateDayStatistics(TransactionDay(6), AccountId("c"), max = TA(1.0), average = TA(1.0)  , aaTotal = TA(0.0)  , ccTotal = TA(0.0)  , ffTotal = TA(1.0)),

          AggregateDayStatistics(TransactionDay(7), AccountId("a"), max = TA(5.0), average = TA(3.67) , aaTotal = TA(8.0)  , ccTotal = TA(11.0) , ffTotal = TA(14.0)),
          AggregateDayStatistics(TransactionDay(7), AccountId("b"), max = TA(8.0), average = TA(4.67) , aaTotal = TA(17.0) , ccTotal = TA(14.0) , ffTotal = TA(11.0)),
          AggregateDayStatistics(TransactionDay(7), AccountId("c"), max = TA(1.0), average = TA(1.0)  , aaTotal = TA(0.0)  , ccTotal = TA(0.0)  , ffTotal = TA(1.0)),

          AggregateDayStatistics(TransactionDay(8), AccountId("a"), max = TA(5.0), average = TA(4.0)  , aaTotal = TA(6.0)  , ccTotal = TA(8.0)  , ffTotal = TA(10.0)),
          AggregateDayStatistics(TransactionDay(8), AccountId("b"), max = TA(8.0), average = TA(5.0)  , aaTotal = TA(12.0) , ccTotal = TA(8.0)  , ffTotal = TA(10.0)),
          AggregateDayStatistics(TransactionDay(8), AccountId("c"), max = TA(3.0), average = TA(2.5)  , aaTotal = TA(5.0)  , ccTotal = TA(0.0)  , ffTotal = TA(0.0)),
        )
      }
    }
  }
}
