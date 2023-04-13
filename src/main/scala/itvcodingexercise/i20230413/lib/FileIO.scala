package itvcodingexercise.i20230413.lib

import java.io.File
import java.nio.file.Files

trait FileIO {
  def writeTemporaryFile(bytes: Seq[Byte], filename: String): File
}

object RealFileIO extends FileIO {
  def writeTemporaryFile(bytes: Seq[Byte], filename: String): File = {
    val tempDir = Files.createTempDirectory(s"itv-coding-exercise-writeTemporaryFile").toFile
    val destination = new File(tempDir, filename)
    Files.write(destination.toPath, bytes.toArray)
    destination
  }
}
