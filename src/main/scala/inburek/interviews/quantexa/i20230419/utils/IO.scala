package inburek.interviews.quantexa.i20230419.utils

import com.typesafe.scalalogging.LazyLogging
import inburek.interviews.quantexa.i20230419.domain.Transaction

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}
import scala.io.Source
import scala.util.{Failure, Success, Using}

object IO extends LazyLogging {
  def writeToConsoleWithoutHeader[Row](data: Seq[Row])(implicit format: CsvWriter[Row]): Unit = {
    format.writeRowsWithoutHeader(data).foreach(println)
  }

  def writeToFileWithHeader[Row](path: Path, data: Seq[Row])(implicit format: CsvWriter[Row]): Unit = {
    val bytes: Array[Byte] = format.writeRowsWithHeader(data).mkString("\n").getBytes(StandardCharsets.UTF_8)
    Files.write(path, bytes)
  }

  def readTransactions(file: File): Either[String, Seq[Transaction]] = {
    val format = implicitly[CsvReader[Transaction]]

    for {
      rawLines <- readLinesFromFileIntoMemory(file)
      transactions <- format.readRowsWithHeader(rawLines)
    } yield transactions
  }

  private def readLinesFromFileIntoMemory(file: File): Either[String, Seq[String]] = {
    Using(Source.fromFile(file))(_.getLines().toSeq) match {
      case Failure(exception) =>
        val message = s"Failed to read lines from file: $file"
        logger.error(message, exception)
        Left(message)
      case Success(rawLines) => Right(rawLines)
    }
  }
}
