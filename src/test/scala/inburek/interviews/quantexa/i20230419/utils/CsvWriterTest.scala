package inburek.interviews.quantexa.i20230419.utils

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

final class CsvWriterTest extends AnyFreeSpecLike {
  "CsvWriter" - {
    ".writeRowsWithHeader" - {
      "works for no rows" in {
        val rows = Seq.empty
        val actual = CsvWriterTest.CsvRow.CsvRowCsvWriter.writeRowsWithHeader(rows)

        actual shouldBe Seq(
          """field 1,field 2"""
        )
      }
      "works for two rows" in {
        val rows = Seq(
          CsvWriterTest.CsvRow("value 1", "value 2"),
          CsvWriterTest.CsvRow("value 3", "value 4"),
        )
        val actual = CsvWriterTest.CsvRow.CsvRowCsvWriter.writeRowsWithHeader(rows)

        actual shouldBe Seq(
          "field 1,field 2",
          "value 1,value 2",
          "value 3,value 4"
        )
      }
    }

    ".writeRowsWithoutHeader" - {
      "works for no rows" in {
        val rows = Seq.empty
        val actual = CsvWriterTest.CsvRow.CsvRowCsvWriter.writeRowsWithoutHeader(rows)

        actual shouldBe Seq.empty
      }

      "works for two rows" in {
        val rows = Seq(
          CsvWriterTest.CsvRow("value 1", "value 2"),
          CsvWriterTest.CsvRow("value 3", "value 4"),
        )
        val actual = CsvWriterTest.CsvRow.CsvRowCsvWriter.writeRowsWithoutHeader(rows)

        actual shouldBe Seq(
          "value 1,value 2",
          "value 3,value 4"
        )
      }
    }
  }
}

object CsvWriterTest {
  private final case class CsvRow(field1: String, field2: String)
  private object CsvRow {
    implicit object CsvRowCsvWriter extends CsvWriter[CsvRow] {
      val headers: Seq[String] = List("field 1", "field 2")
      def writeRow(row: CsvRow): String = s"${row.field1},${row.field2}"
    }
  }
}
