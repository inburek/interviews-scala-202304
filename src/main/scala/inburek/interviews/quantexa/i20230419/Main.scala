package inburek.interviews.quantexa.i20230419

import com.typesafe.scalalogging.LazyLogging
import inburek.interviews.quantexa.i20230419.dataprocessing.{Question1, Question2, Question3}
import inburek.interviews.quantexa.i20230419.utils.IO

import java.io.File
import java.nio.file.Path

object Main extends LazyLogging {
  /** Runs the Scala exercise for all 3 questions and outputs them to `./outputs/`. (Outputs already committed)
    *
    * The assignment didn't provide any consumable validation data, so there are almost certainly bugs or misunderstandings.
    * I generated some sample data and validated it with Google Spreadsheets, as seen in `Question3Test`.
    *
    * Since no libraries could be used (e.g. AkkaStreams), I chose not to stream the data iterators don't
    *   guarantee closing of the underlying sources. Therefore, all the data is read into memory before processing.
    * Since the O(1) memory was no longer an option, I didn't bother with many optimisations.
    *
    * The exercise required me to use Double for the amounts. I would have preferred BigDecimal.
    *
    * @param args unused
    */
  def main(args: Array[String]): Unit = {
    val transactionsUrl: File = new File("./src/main/resources/transactions.txt")

    val maybeTransactions = IO.readTransactions(transactionsUrl)
    maybeTransactions match {
      case Left(error) =>
        logger.error(error)
        sys.exit(1)
      case Right(transactions) =>
        val outputDirectory = Path.of("./outputs")

        val question1Path: Path = {
          val outputPath = outputDirectory.resolve("question1.csv")
          val result = Question1.totalPerDay(transactions)
          IO.writeToFileWithHeader(outputPath, result)
          outputPath
        }

        val question2Path: Path = {
          val outputPath = outputDirectory.resolve("question2.csv")
          val result = Question2.averageTransactionsPerAccount(transactions)
          IO.writeToFileWithHeader(outputPath, result)
          outputPath
        }

        val question3Path: Path = {
          val outputPath = outputDirectory.resolve("question3.csv")
          val result = Question3.movingStatistics(transactions)
          IO.writeToFileWithHeader(outputPath, result)
          outputPath
        }

        logger.info(s"Question 1 results written to $question1Path")
        logger.info(s"Question 2 results written to $question2Path")
        logger.info(s"Question 3 results written to $question3Path")
    }
  }
}
