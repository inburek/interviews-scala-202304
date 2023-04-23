package inburek.interviews.quantexa.i20230419.dataprocessing

import inburek.interviews.quantexa.i20230419.domain._

object Question2 {
  def averageTransactionsPerAccount(transactions: Seq[Transaction]): Seq[TransactionAveragePerAccount] = {
    transactions
      .foldLeft(AverageAggregate.empty)((acc, transaction) => acc.add(transaction))
      .results
  }

  private final case class AverageAggregate(totals: Map[AccountId, Map[TransactionCategory, (TransactionAmount, Int)]]) {
    def add(transaction: Transaction): AverageAggregate = {
      val totalsPerAccount = totals.getOrElse(transaction.accountId, Map.empty)

      val (oldTotal, oldCount) = totalsPerAccount.getOrElse(transaction.category, (TransactionAmount.empty, 0))

      val newTotalsPerAccount: Map[TransactionCategory, (TransactionAmount, Int)] =
        totalsPerAccount + (transaction.category -> (oldTotal + transaction.amount, oldCount + 1))

      copy(totals = totals + (transaction.accountId -> newTotalsPerAccount))
    }

    def results: Seq[TransactionAveragePerAccount] = {
      totals
        .map { case (accountId, totals) =>
          val totalsPerAccount = totals.map { case (category, (total, count)) =>
            category -> TransactionAmount(total.value / count)
          }
          TransactionAveragePerAccount(accountId, totalsPerAccount)
        }
        .toSeq
        .sortBy(_.account.value)
    }
  }

  private object AverageAggregate {
    def empty: AverageAggregate = AverageAggregate(Map.empty)
  }
}
