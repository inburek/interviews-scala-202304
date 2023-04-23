package inburek.interviews.quantexa.i20230419.domain

import inburek.interviews.quantexa.i20230419.utils.CsvWriter

final case class TransactionTotalInADay(day: TransactionDay, total: TransactionAmount)

object TransactionTotalInADay {
  implicit object TransactionTotalInADayFormat extends CsvWriter[TransactionTotalInADay] {
    val headers: List[String] = List("day", "totalAmount")

    def writeRow(row: TransactionTotalInADay): String =
      List(row.day, row.total).mkString(",")
  }
}
