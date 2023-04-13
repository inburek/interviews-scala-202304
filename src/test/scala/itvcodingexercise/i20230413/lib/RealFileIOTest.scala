package itvcodingexercise.i20230413.lib

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import java.nio.file.Files

final class RealFileIOTest extends AnyFreeSpecLike {
  "RealFileIO" - {
    ".writeTemporaryFile" - {
      "writes a non-empty text file correctly" in {
        val bytes = "Hello, world!".getBytes("UTF-8")
        val filename = "hello-world.txt"

        val destination = RealFileIO.writeTemporaryFile(bytes, filename)

        destination.isFile shouldBe true
        Files.readAllBytes(destination.toPath) shouldBe bytes
      }
    }
  }
}
