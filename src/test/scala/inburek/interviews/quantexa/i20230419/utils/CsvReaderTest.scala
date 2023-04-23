package inburek.interviews.quantexa.i20230419.utils

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

final class CsvReaderTest extends AnyFreeSpecLike {
  "CsvReader" - {
    ".readRowsWithHeader" - {
      "works for a valid header and no rows" in {
        val result = CsvReaderTest.CsvRow.CsvRowCsvReader.readRowsWithHeader(Seq(
          "field 1,field 2"
        ))
        result shouldBe Right(Seq.empty)
      }

      "works for a valid header and two valid rows" in {
        val result = CsvReaderTest.CsvRow.CsvRowCsvReader.readRowsWithHeader(Seq(
          "field 1,field 2",
          "value 1,value 2",
          "value 3,value 4"
        ))
        result shouldBe Right(
          Seq(
            CsvReaderTest.CsvRow("value 1", "value 2"),
            CsvReaderTest.CsvRow("value 3", "value 4")
          )
        )
      }

      "works for a valid header and two invalid rows" in {
        val result = CsvReaderTest.CsvRow.CsvRowCsvReader.readRowsWithHeader(Seq(
          "field 1,field 2",
          "iminvalid",
          "value 3,value 4"
        ))
        result shouldBe Left("Expected 2 fields, got: iminvalid")
      }

      "works for an invalid header and no rows" in {
        val result = CsvReaderTest.CsvRow.CsvRowCsvReader.readRowsWithHeader(Seq(
          "field 1,field 3"
        ))
        result shouldBe Left("Expected headers: List(field 1, field 2); got: List(field 1,field 3)")
      }

      "works for an invalid header and two valid rows" in {
        val result = CsvReaderTest.CsvRow.CsvRowCsvReader.readRowsWithHeader(Seq(
          "field 1,field 3",
          "value 1,value 2",
          "value 3,value 4"
        ))
        result shouldBe Left("Expected headers: List(field 1, field 2); got: List(field 1,field 3)")
      }
    }
    ".readRowsWithoutHeader" - {
      "works for no rows" in {
        val result = CsvReaderTest.CsvRow.CsvRowCsvReader.readRowsWithoutHeader(Seq.empty)
        result shouldBe Right(Seq.empty)
      }

      "works for two valid rows" in {
        val result = CsvReaderTest.CsvRow.CsvRowCsvReader.readRowsWithoutHeader(Seq(
          "value 1,value 2",
          "value 3,value 4"
        ))
        result shouldBe Right(
          Seq(
            CsvReaderTest.CsvRow("value 1", "value 2"),
            CsvReaderTest.CsvRow("value 3", "value 4")
          )
        )
      }

      "works for two invalid rows" in {
        val result = CsvReaderTest.CsvRow.CsvRowCsvReader.readRowsWithoutHeader(Seq(
          "iminvalid",
          "value 3,value 4"
        ))
        result shouldBe Left("Expected 2 fields, got: iminvalid")
      }
    }
  }
}

object CsvReaderTest {
  private final case class CsvRow(field1: String, field2: String)
  private object CsvRow {
    implicit object CsvRowCsvReader extends CsvReader[CsvRow] {
      val headers: Seq[String] = List("field 1", "field 2")
      def readRow(line: String): Either[String, CsvRow] = {
        line.split(",").toList match {
          case field1 :: field2 :: Nil => Right(CsvRow(field1, field2))
          case _ => Left(s"Expected 2 fields, got: $line")
        }
      }
    }
  }
}
