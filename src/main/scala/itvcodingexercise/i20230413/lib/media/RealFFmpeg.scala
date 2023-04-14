package itvcodingexercise.i20230413.lib.media

import com.typesafe.scalalogging.LazyLogging

import java.io.File
import scala.sys.process._

final class RealFFmpeg() extends FFmpeg with LazyLogging {
  def version(): String = {
    val command: Seq[String] = Seq("ffmpeg", "-version")
    logger.debug(s"""Running command: "${command.mkString(" ")}"""") // TODO: Quote the strings to spot spaces in args.

    command.!!
  }

  def makeThumbnail(videoFile: File, offset: String, destination: File): Unit = {
    val command: Seq[String] = Seq("ffmpeg", "-i", videoFile.getAbsolutePath, "-ss", offset, "-vframes", "1", destination.getAbsolutePath)
    logger.debug(s"""Running command: "${command.mkString(" ")}"""") // TODO: Quote the strings to spot spaces in args.

    command.!!
  }
}
