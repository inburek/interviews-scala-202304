package inburek.interviews.quantexa.i20230419.domain

import inburek.interviews.quantexa.i20230419.utils.CsvWriter

final case class TransactionAveragePerAccount(account: AccountId, totals: Map[TransactionCategory, TransactionAmount])

object TransactionAveragePerAccount {
  implicit object TransactionTotalInADayFormat extends CsvWriter[TransactionAveragePerAccount] {
    val headers: List[String] = List("Account ID") ++ TransactionCategory.allKnown.map(category => s"${category.value} total")

    def writeRow(row: TransactionAveragePerAccount): String = {
      val totalsFields = TransactionCategory.allKnown.map(row.totals.getOrElse(_, TransactionAmount.empty))
      (row.account +: totalsFields).mkString(",")
    }
  }
}
