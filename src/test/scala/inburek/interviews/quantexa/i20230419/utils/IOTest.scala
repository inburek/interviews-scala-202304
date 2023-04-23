package inburek.interviews.quantexa.i20230419.utils

import com.typesafe.scalalogging.LazyLogging
import inburek.interviews.quantexa.i20230419.domain._
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

import java.nio.charset.StandardCharsets
import java.nio.file.Files

/** These are not unit tests, but we have to check that this works.
  */
final class IOTest extends AnyFreeSpecLike with LazyLogging {
  "IO" - {
    ".readTransactions" in {
      val csvPath = Files.createTempFile("FileReadingTest-readTransactions-", ".csv")
      val csvFile = csvPath.toFile
      logger.info(s"Using temporary file for testing: $csvFile")

      Files.write(
        csvPath,
        """transactionId,accountId,transactionDay,category,transactionAmount
          |T0001,A27,1,GG,338.11
          |T0002,A5,1,BB,677.89
          |T0003,A32,1,DD,499.86
          |T0004,A42,1,DD,801.81
          |T0005,A19,1,BB,14.42
          |""".stripMargin.trim.getBytes(StandardCharsets.UTF_8)
      )

      val transactions = IO.readTransactions(csvFile).getOrElse(fail("Expected to read transactions"))
      transactions shouldBe List(
        Transaction(TransactionId("T0001"), AccountId("A27"), TransactionDay(1), TransactionCategory("GG"), TransactionAmount(338.11)),
        Transaction(TransactionId("T0002"), AccountId("A5"), TransactionDay(1), TransactionCategory("BB"), TransactionAmount(677.89)),
        Transaction(TransactionId("T0003"), AccountId("A32"), TransactionDay(1), TransactionCategory("DD"), TransactionAmount(499.86)),
        Transaction(TransactionId("T0004"), AccountId("A42"), TransactionDay(1), TransactionCategory("DD"), TransactionAmount(801.81)),
        Transaction(TransactionId("T0005"), AccountId("A19"), TransactionDay(1), TransactionCategory("BB"), TransactionAmount(14.42)),
      )
    }
  }
}
