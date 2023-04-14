package itvcodingexercise.i20230413

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.Uri.{Authority, Host, Path}
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, StatusCodes, Uri}
import akka.stream.Materializer
import itvcodingexercise.i20230413.RealItvApi.{VideoData, VideoMetadata}
import itvcodingexercise.i20230413.lib.Hashing
import itvcodingexercise.i20230413.lib.http.{AkkaHttp, StrictHttpResponse}
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import scala.concurrent.{ExecutionContext, Future}

trait ItvApi {
  def videoMetadata(assetId: String)
                   (implicit ec: ExecutionContext, mat: Materializer): Future[VideoMetadata]

  def videoData(assetId: String)(implicit ec: ExecutionContext, mat: Materializer): Future[VideoData]

  final def videoFileValidated(assetId: String)(implicit ec: ExecutionContext, mat: Materializer): Future[VideoData] = {
    for {
      metadata <- videoMetadata(assetId)
      videoData <- videoData(assetId)
    } yield {
      val actualSha256: String = Hashing.sha256(videoData.bytes)
      val expectedSha256: String = metadata.sha256

      if (actualSha256 == expectedSha256) {
        videoData
      } else {
        throw new RuntimeException(
          "SHA-256 checksum of downloaded video failed. " +
          s"For video asset '$assetId', expected '$expectedSha256', got '$actualSha256'."
        )
      }
    }
  }
}

final class RealItvApi(akkaHttp: AkkaHttp) extends ItvApi {
  private val itvHost = Host("cdfr062ui5.execute-api.eu-west-1.amazonaws.com")

  def videoMetadata(assetId: String)(implicit ec: ExecutionContext, mat: Materializer): Future[VideoMetadata] = {
    val path = Path / "playground" / assetId / "metadata"

    val uri = Uri(authority = Authority(host = itvHost), path = path, scheme = "https")

    akkaHttp.singleStrictRequest[VideoMetadata](HttpRequest(HttpMethods.GET, uri))
  }

  def videoData(assetId: String)
               (implicit ec: ExecutionContext, mat: Materializer): Future[VideoData] = {
    val path = Path / "playground" / assetId
    val uri = Uri(authority = Authority(host = itvHost), path = path, scheme = "https")

    akkaHttp
      .strictRequest(HttpRequest(HttpMethods.GET, uri))
      .map {
        case StrictHttpResponse(strictResponse, strictEntity) if strictResponse.status == StatusCodes.OK =>
          VideoData(strictEntity.data)
        case failedResponse =>
          throw new RuntimeException(s"Failed to download video data for asset '$assetId': $failedResponse")
      }
  }
}

object RealItvApi {
  final case class VideoMetadata(sha256: String)
  object VideoMetadata {
    implicit val jsonFormat: RootJsonFormat[VideoMetadata] = jsonFormat1(VideoMetadata.apply)
  }

  final case class VideoData(bytes: Seq[Byte])
}
