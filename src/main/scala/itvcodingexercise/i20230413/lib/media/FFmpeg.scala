package itvcodingexercise.i20230413.lib.media

import scala.sys.process._

final class FFmpeg() {
  def version(): String = {
    "ffmpeg -version".!!
  }
}
