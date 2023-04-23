package inburek.interviews.quantexa.i20230419.domain

import inburek.interviews.quantexa.i20230419.utils.CsvFormat
import inburek.interviews.quantexa.i20230419.utils.OptionOps.RichOption

final case class Transaction(transactionId: TransactionId,
                             accountId: AccountId,
                             transactionDay: TransactionDay,
                             category: TransactionCategory,
                             amount: TransactionAmount)

object Transaction {
  implicit object TransactionsCsvFormat extends CsvFormat[Transaction] {
    val headers: List[String] = List("transactionId", "accountId", "transactionDay", "category", "transactionAmount")

    def writeRow(row: Transaction): String =
      List(row.transactionId, row.accountId, row.transactionDay, row.category, row.amount).mkString(",")

    def readRow(line: String): Either[String, Transaction] = {
      val fields = line.split(",").map(_.trim).toSeq

      fields match {
        case Seq(id, accountId, transactionDayString, category, amountString) =>
          for {
            transactionDay <- transactionDayString.toIntOption.toEither(s"transactionDay is not an integer: '$transactionDayString'")
            amount <- amountString.toDoubleOption.toEither(s"amount is not a double: '$amountString'")
            transaction = Transaction(
              TransactionId(id),
              AccountId(accountId),
              TransactionDay(transactionDay),
              TransactionCategory(category),
              TransactionAmount(amount)
            )
          } yield transaction
        case wrongNumber =>
          Left(s"Expected 5 fields, got ${wrongNumber.size}: $wrongNumber")
      }
    }

  }
}
