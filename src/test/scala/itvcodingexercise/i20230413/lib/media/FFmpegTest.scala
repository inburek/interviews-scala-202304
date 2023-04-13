package itvcodingexercise.i20230413.lib.media

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

import java.io.File
import java.nio.file.Files
import scala.concurrent.duration.DurationInt

/** Runs external commands to check that [[FFmpeg]] works as expected.
  *
  * This is also not a unit test because it has external dependencies,
  *   but we need to test this somewhere and we don't have a second module to stick this in.
  */
final class FFmpegTest extends AnyFreeSpecLike with LazyLogging {
  "FFmpeg" - {
    ".version" - {
      "should return the version of ffmpeg" in {
        val ffmpeg = new FFmpeg()

        val version = ffmpeg.version()

        version should startWith ("ffmpeg version ")
      }
    }

    ".makeThumbnail" - {
      "should create a thumbnail from a video file" in {
        val ffmpeg = new FFmpeg()

        val videoFile = new File(getClass.getClassLoader.getResource("samples/valid.mov").getFile)

        val tempDir = Files.createTempDirectory(s"itv-coding-exercise-ffmpeg").toFile
        val destination = new File(tempDir, "valid.thumbnail.png")

        logger.debug(s"""About to create thumbnail: ${destination.getAbsolutePath}""")
        ffmpeg.makeThumbnail(videoFile = videoFile, offset = "0:0:1", destination = destination)

        destination.exists() shouldBe true
        // We're not testing that the image contains the correct frame.
        // We could do that if we crafted a specific video file for this test, with a distinctive frame.
      }
    }
  }

}
