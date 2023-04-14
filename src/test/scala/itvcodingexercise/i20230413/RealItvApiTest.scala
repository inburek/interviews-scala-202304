package itvcodingexercise.i20230413

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.stream.Materializer
import akka.util.ByteString
import itvcodingexercise.i20230413.RealItvApi.{VideoData, VideoMetadata}
import itvcodingexercise.i20230413.lib.http.{AkkaHttp, StrictHttpResponse}
import itvcodingexercise.i20230413.testutils.SameThreadExecutionContext
import org.scalamock.scalatest.MockFactory
import org.scalatest.Outcome
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.freespec.FixtureAnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt
import scala.util.Using

final class RealItvApiTest extends FixtureAnyFreeSpecLike with MockFactory {
  "RealItvApi" - {
    ".videoMetadata" - {
      "works for a successful download" in { f =>
        import f._

        val akkaHttp = stub[AkkaHttp]
        (() => akkaHttp.system).when().returns(sys)
        (akkaHttp.strictRequest _).when(*).returns(StrictHttpResponse(videoMetadataResponseStrict, 0.seconds))

        val api = new RealItvApi(akkaHttp)

        val eventualMetadata = api.videoMetadata("my-video-id")

        (akkaHttp.strictRequest _).verify(HttpRequest(uri = "https://cdfr062ui5.execute-api.eu-west-1.amazonaws.com/playground/my-video-id/metadata")).once()
        eventualMetadata.futureValue shouldBe videoMetadataAsObject
      }
    }

    ".videoFile" - {
      "for a successful download" in { f =>
        import f._

        val akkaHttp = stub[AkkaHttp]
        (() => akkaHttp.system).when().returns(sys)
        (akkaHttp.strictRequest _).when(*).returns(StrictHttpResponse(videoDataResponseStrict, 0.seconds))

        val api = new RealItvApi(akkaHttp)

        val eventualMetadata = api.videoData("my-video-id")

        (akkaHttp.strictRequest _).verify(HttpRequest(uri = "https://cdfr062ui5.execute-api.eu-west-1.amazonaws.com/playground/my-video-id")).once()
        eventualMetadata.futureValue shouldBe videoDataObject
      }

      "for a bad HTTP status" in { f =>
        import f._

        val akkaHttp = stub[AkkaHttp]
        (() => akkaHttp.system).when().returns(sys)
        (akkaHttp.strictRequest _).when(*).returns(StrictHttpResponse(HttpResponse(status = StatusCodes.NotFound), 0.seconds))

        val api = new RealItvApi(akkaHttp)

        val eventualMetadata = api.videoData("my-video-id")

        (akkaHttp.strictRequest _).verify(HttpRequest(uri = "https://cdfr062ui5.execute-api.eu-west-1.amazonaws.com/playground/my-video-id")).once()
        eventualMetadata.failed.futureValue shouldBe a[RuntimeException]
      }
    }
  }


  protected def withFixture(test: OneArgTest): Outcome =
    Using(new FixtureParam)(fixture => super.withFixture(test.toNoArgTest(fixture))).get

  final class FixtureParam extends AutoCloseable {
    implicit val ec: ExecutionContext = SameThreadExecutionContext
    implicit val sys: ActorSystem = ActorSystem("test-actor-system", defaultExecutionContext = Some(ec))
    implicit val mat: Materializer = Materializer(sys)

    def close(): Unit = sys.terminate()
  }

  private def videoDataResponseStrict: HttpResponse = {
    HttpResponse(entity = HttpEntity.Strict(ContentType(MediaTypes.`video/quicktime`), ByteString(videoDataObject.bytes)))
  }

  private def videoDataObject: VideoData = VideoData(Seq[Byte](0x66, 0x67, 0x68))

  private def videoMetadataResponseStrict: HttpResponse = {
    HttpResponse(entity = HttpEntity.Strict(ContentTypes.`application/json`, ByteString(videoMetadataResponseJson)))
  }

  private def videoMetadataAsObject: VideoMetadata =
    VideoMetadata(sha256 = "6f314c1d10090f43422756a0451509a8481d87a0a9e3a26ca073a98af5523247")

  /** Example taken from:
    *   https://cdfr062ui5.execute-api.eu-west-1.amazonaws.com/playground/valid/metadata
    *   on 14/04/2023.
    */
  private def videoMetadataResponseJson: String =
    """{
      |	"sha1": "35ef526d6b5cf4bb8ec93082c8f653a9a84b6e52",
      |	"sha256": "6f314c1d10090f43422756a0451509a8481d87a0a9e3a26ca073a98af5523247",
      |	"md5": "c56ce4bd3047740e578e6f05ea533a8c",
      |	"crc32": "4367ebd3",
      |	"videoQuality": {
      |		"frameRate": "25FPS",
      |		"resolution": "HD",
      |		"dynamicRange": "HDR"
      |	},
      |	"identifiers": {
      |		"productionId": "1/1111/1234#001",
      |		"title": "My Pretty Cat",
      |		"seriesNumber": null,
      |		"episodeNumber": null,
      |		"duration": "00:00:09"
      |	}
      |}
      |""".stripMargin
}
