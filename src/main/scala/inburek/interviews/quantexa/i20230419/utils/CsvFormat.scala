package inburek.interviews.quantexa.i20230419.utils

trait CsvFormat[Row] extends CsvWriter[Row] with CsvReader[Row]

// Takes a type parameter to be sure we don't mix up the headers when we use implicits
trait CsvHeaderDefinition[Row] {
  def headers: Seq[String]

  final def headerString: String = headers.mkString(",")
}

trait CsvWriter[Row] extends CsvHeaderDefinition[Row] {
  def writeRow(row: Row): String

  final def writeRowsWithHeader(rows: Seq[Row]): Seq[String] =
    headerString +: writeRowsWithoutHeader(rows)

  final def writeRowsWithoutHeader(rows: Seq[Row]): Seq[String] =
    rows.map(writeRow)
}

trait CsvReader[Row] extends CsvHeaderDefinition[Row] {
  def readRow(line: String): Either[String, Row]

  final def readRowsWithHeader(rows: Seq[String]): Either[String, Seq[Row]] = {
    val (actualHeaderRows, dataLines) = rows.splitAt(1)
    if (actualHeaderRows.toList == List(this.headerString)) {
      readRowsWithoutHeader(dataLines)
    } else {
      Left(s"Expected headers: ${this.headers}; got: $actualHeaderRows")
    }
  }

  final def readRowsWithoutHeader(rows: Seq[String]): Either[String, Seq[Row]] = {
    rows
      .map(readRow)
      .foldLeft[Either[String, Seq[Row]]](Right(Seq.empty)) { (result, maybeRow) =>
        for {
          accValue <- result
          nextValue <- maybeRow
        } yield accValue :+ nextValue
      }
  }
}
