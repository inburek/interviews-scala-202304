package inburek.interviews.quantexa.i20230419.dataprocessing

import inburek.interviews.quantexa.i20230419.domain.{Transaction, TransactionTotalInADay}

object Question1 {
  /** Assumes that the transactions are sorted by day. */
  def totalPerDay(transactions: Seq[Transaction]): Seq[TransactionTotalInADay] = {
    transactions
      .foldLeft(TransactionTotalAggregate.empty)((acc, transaction) => acc.add(transaction))
      .result
  }

  private final case class TransactionTotalAggregate(previousDays: Seq[TransactionTotalInADay], maybeCurrentDay: Option[TransactionTotalInADay]) {
    /** Assumes that the transactions are sorted by day. */
    def add(transaction: Transaction): TransactionTotalAggregate = {
      maybeCurrentDay match {
        case None =>
          copy(maybeCurrentDay = Some(TransactionTotalInADay(day = transaction.transactionDay, total = transaction.amount)))
        case Some(currentDay) =>
          if (currentDay.day == transaction.transactionDay) {
            copy(maybeCurrentDay = Some(currentDay.copy(total = currentDay.total + transaction.amount)))
          } else {
            TransactionTotalAggregate(
              previousDays :+ currentDay,
              Some(TransactionTotalInADay(transaction.transactionDay, transaction.amount))
            )
          }
      }
    }

    def result: Seq[TransactionTotalInADay] = previousDays ++ maybeCurrentDay.toSeq
  }

  private object TransactionTotalAggregate {
    def empty: TransactionTotalAggregate = TransactionTotalAggregate(Seq.empty, None)
  }
}
