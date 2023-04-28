package itvcodingexercise.i20230413

import akka.stream.Materializer
import com.typesafe.scalalogging.LazyLogging
import itvcodingexercise.i20230413.ItvVideoProcessing.ProcessingArgs
import itvcodingexercise.i20230413.lib.FileIO
import itvcodingexercise.i20230413.lib.media.FFmpeg

import java.io.File
import scala.concurrent.{ExecutionContext, Future}

final class ItvVideoProcessing(itvApi: ItvApi, fileIO: FileIO, ffmpeg: FFmpeg) extends LazyLogging {

  def makeThumbnail(args: ProcessingArgs)
                   (implicit ec: ExecutionContext, mat: Materializer): Future[Unit] = {
    for {
      video <- itvApi.videoFileValidated(args.videoAssetId)
      videoFile <- Future(fileIO.writeTemporaryFile(video.bytes, filename = "downloaded.mov"))
      () <- Future(ffmpeg.makeThumbnail(videoFile, offset = args.thumbnailTimestamp, destination = new File(args.destination)))
    } yield ()
  }
}

object ItvVideoProcessing {
  final case class ProcessingArgs(videoAssetId: String, thumbnailTimestamp: String, destination: String)
}
