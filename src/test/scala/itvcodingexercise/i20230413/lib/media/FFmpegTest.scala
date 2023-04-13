package itvcodingexercise.i20230413.lib.media

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

/** Runs external commands to check that [[FFmpeg]] works as expected.
  *
  * This is also not a unit test because it has external dependencies,
  *   but we need to test this somewhere and we don't have a second module to stick this in.
  */
final class FFmpegTest extends AnyFreeSpecLike {
  "FFmpeg" - {
    ".version" - {
      "should return the version of ffmpeg" in {
        val ffmpeg = new FFmpeg()

        val version = ffmpeg.version()

        version should startWith ("ffmpeg version ")
      }
    }
  }

}
