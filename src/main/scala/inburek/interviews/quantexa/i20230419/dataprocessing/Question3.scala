package inburek.interviews.quantexa.i20230419.dataprocessing

import com.typesafe.scalalogging.LazyLogging
import inburek.interviews.quantexa.i20230419.domain._

object Question3 extends LazyLogging {

  def movingStatistics(transactions: Seq[Transaction]): Seq[AggregateDayStatistics] = {
    logger.debug(s"Starting moving statistics of transactions for ${transactions.size} transactions")
    if (transactions.isEmpty) {
      logger.debug("No transactions; returning empty.")
      Seq.empty
    } else {
      val transactionsByDay: Map[TransactionDay, Seq[Transaction]] = transactions.groupBy(_.transactionDay)
      logger.debug(s"Transactions found on days [${transactionsByDay.keySet.toSeq.map(_.value).sorted.mkString(", ")}]")

      val minDayEntryValue: Int = transactionsByDay.keySet.minBy(_.value).value + 1 // days 1,2,3,4,5 => 2
      val maxDayEntryValue: Int = transactionsByDay.keySet.maxBy(_.value).value + 1 // days 1,2,3,4,5,6,7 => 8

      logger.debug(s"Statistics entries between days $minDayEntryValue and $maxDayEntryValue")

      val result =
        (minDayEntryValue to maxDayEntryValue)
          .flatMap[AggregateDayStatistics] { logDayNumber =>

            val earliestDayToConsider = logDayNumber - 5 // if today is 10, then 5 == 10-5
            val daysToConsider: Seq[TransactionDay] = (earliestDayToConsider until logDayNumber).map(TransactionDay(_))

            logger.debug(s"For day $logDayNumber, considering days [${daysToConsider.map(_.value).mkString(", ")}]")

            val transactions: Seq[Transaction] = daysToConsider.flatMap(transactionsByDay.get).flatten

            transactions
              .foldLeft(DayAggregates.empty)(_.addBlindlly(_))
              .results(TransactionDay(logDayNumber))
          }

      logger.debug(s"Outputting ${result.size} logs for statistics.")
      result
    }
  }

  private final case class DayAggregates(data: Map[AccountId, DayAccountAggregate]) {
    /** Ignores the day. */
    def addBlindlly(transaction: Transaction): DayAggregates = {
      val accountId = transaction.accountId
      val oldDayAccountAggregate = data.getOrElse(accountId, DayAccountAggregate.empty)
      val newDayAccountAggregate = oldDayAccountAggregate.addBlindly(transaction)

      DayAggregates(data + ((accountId, newDayAccountAggregate)))
    }

    def results(day: TransactionDay): Seq[AggregateDayStatistics] = {
      data
        .flatMap {
          case (accountId, aggregate) => aggregate.result(day, accountId)
        }
        .toSeq
    }
  }

  private object DayAggregates {
    def empty: DayAggregates = DayAggregates(Map.empty)
  }

  private final case class DayAccountAggregate(count: Int, byCategory: Map[TransactionCategory, TransactionAmount], max: TransactionAmount) {
    /** Ignores the day and account ID */
    def addBlindly(transaction: Transaction): DayAccountAggregate = {
      val category = transaction.category
      val oldAmountForCategory = byCategory.getOrElse(category, TransactionAmount(0))

      val newCount = count + 1
      val newByCategory = byCategory + (category -> (oldAmountForCategory + transaction.amount))
      val newMax = TransactionAmount(Math.max(transaction.amount.value, max.value))

      DayAccountAggregate(count = newCount, byCategory = newByCategory, max = newMax)
    }

    def result(day: TransactionDay, accountId: AccountId): Option[AggregateDayStatistics] = {
      logger.debug(s"Calculating results for day $day, $accountId, $this")

      if (count > 0) {
        val total = byCategory.values.foldLeft(TransactionAmount(0))(_ + _)

        Some(AggregateDayStatistics(
          day,
          accountId,
          max = max,
          average = TransactionAmount(total.value / count),
          aaTotal = byCategory.getOrElse(TransactionCategory.categoryAA, TransactionAmount(0)),
          ccTotal = byCategory.getOrElse(TransactionCategory.categoryCC, TransactionAmount(0)),
          ffTotal = byCategory.getOrElse(TransactionCategory.categoryFF, TransactionAmount(0)),
        ))
      } else {
        None
      }
    }
  }

  private object DayAccountAggregate {
    def empty: DayAccountAggregate = DayAccountAggregate(count = 0, byCategory = Map.empty, max = TransactionAmount(0))
  }
}
