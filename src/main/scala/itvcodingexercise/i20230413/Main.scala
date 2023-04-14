package itvcodingexercise.i20230413

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import itvcodingexercise.i20230413.lib.RealFileIO
import itvcodingexercise.i20230413.lib.http.{AkkaHttp, RealAkkaHttp}
import itvcodingexercise.i20230413.lib.media.{FFmpeg, RealFFmpeg}

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration.DurationInt

object Main extends LazyLogging {
  /** Downloads video, validates against checksum, and saves a thumbnail. */
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem()
    try {
      implicit val ec: ExecutionContextExecutor = system.dispatcher

      val akkaHttp: AkkaHttp = new RealAkkaHttp(entityTimeout = 30.seconds)
      val itvApi: ItvApi = new RealItvApi(akkaHttp)
      val fmpeg: FFmpeg = new RealFFmpeg()
      val videoProcessing = new ItvVideoProcessing(itvApi, RealFileIO, fmpeg)

      Await.result(videoProcessing.makeThumbnail(args), 60.seconds)
    } finally {
      system.terminate()
    }
  }
}
