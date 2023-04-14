package itvcodingexercise.i20230413.lib.media

import java.io.File

trait FFmpeg {
  def version(): String
  def makeThumbnail(videoFile: File, offset: String, destination: File): Unit
}
