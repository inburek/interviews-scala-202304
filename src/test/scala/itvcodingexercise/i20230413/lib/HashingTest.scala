package itvcodingexercise.i20230413.lib

import itvcodingexercise.i20230413.testutils.TestFiles
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import java.nio.file.Files

final class HashingTest extends AnyFreeSpecLike {
  "Hashing" - {
    ".sha256" - {
      "works for an empty array" in {
        Hashing.sha256(Seq.empty) shouldBe "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
      }

      "works for a non-empty array" in {
        // Test case generated online:
        // https://emn178.github.io/online-tools/sha256.html
        Hashing.sha256(Seq(0x00, 0x5A, 0x67)) shouldBe "ab852e4d5bd264ba71f340db1f94e33bc86b1fff659ca1ae554f8ddbd23f511c"
      }

      "works on the valid file" in {
        Hashing.sha256(Files.readAllBytes(TestFiles.Videos.validFile.toPath)) shouldBe "6f314c1d10090f43422756a0451509a8481d87a0a9e3a26ca073a98af5523247"
      }
    }
  }
}
