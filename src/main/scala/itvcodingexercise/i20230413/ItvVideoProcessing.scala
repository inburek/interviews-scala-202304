package itvcodingexercise.i20230413

import akka.stream.Materializer
import com.typesafe.scalalogging.LazyLogging
import itvcodingexercise.i20230413.lib.FileIO
import itvcodingexercise.i20230413.lib.media.FFmpeg

import java.io.File
import scala.concurrent.{ExecutionContext, Future}

final class ItvVideoProcessing(itvApi: ItvApi, fileIO: FileIO, ffmpeg: FFmpeg) extends LazyLogging {

  def makeThumbnail(args: Seq[String])
                   (implicit ec: ExecutionContext, mat: Materializer): Future[Unit] = {
    val argsDoc: String = """<video asset ID> <thumbnail timestamp> <destination path/filename>"""

    args match {
      case Seq(videoAssetId: String, thumbnailTimestamp: String, destination: String) =>
        logger.info(s"Video URL: $videoAssetId")
        logger.info(s"Thumbnail timestamp: $thumbnailTimestamp")
        logger.info(s"Destination: $destination")

        for {
          video <- itvApi.videoFileValidated(videoAssetId)
          videoFile <- Future(fileIO.writeTemporaryFile(video.bytes, filename = "downloaded.mov"))
          () <- Future(ffmpeg.makeThumbnail(videoFile, offset = thumbnailTimestamp, destination = new File(destination)))
        } yield ()
      case _ =>
        val errorMessage = s"""Expected 3 arguments ($argsDoc) but got ${args.length}:\n${args.mkString("\n")}"""
        logger.error(errorMessage)
        throw new IllegalArgumentException(errorMessage)
    }
  }
}
