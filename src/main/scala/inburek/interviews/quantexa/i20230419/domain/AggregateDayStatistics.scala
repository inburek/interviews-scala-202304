package inburek.interviews.quantexa.i20230419.domain

import inburek.interviews.quantexa.i20230419.utils.CsvWriter

final case class AggregateDayStatistics(day: TransactionDay,
                                        account: AccountId,
                                        max: TransactionAmount,
                                        average: TransactionAmount,
                                        aaTotal: TransactionAmount,
                                        ccTotal: TransactionAmount,
                                        ffTotal: TransactionAmount)

object AggregateDayStatistics {
  implicit object AggregateDayStatisticsFormat extends CsvWriter[AggregateDayStatistics] {
    val headers: List[String] = List("Day", "Account ID", "Maximum", "Average", "AA Total Value", "CC Total Value", "FF Total Value")

    def writeRow(row: AggregateDayStatistics): String =
      List(row.day, row.account, row.max, row.average, row.aaTotal, row.ccTotal, row.ffTotal).mkString(",")
  }
}
