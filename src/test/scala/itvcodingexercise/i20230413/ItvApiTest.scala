package itvcodingexercise.i20230413

import akka.actor.ActorSystem
import akka.stream.Materializer
import itvcodingexercise.i20230413.RealItvApi.{VideoData, VideoMetadata}
import itvcodingexercise.i20230413.lib.Hashing
import itvcodingexercise.i20230413.testutils.SameThreadExecutionContext
import org.scalamock.scalatest.MockFactory
import org.scalatest.Outcome
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.freespec.FixtureAnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Using

final class ItvApiTest extends FixtureAnyFreeSpecLike with MockFactory {
  "ItvApi" - {
    ".videoFileValidated" - {
      val videoData = VideoData(Seq(0x66, 0x67))
      val invalidMetadata = VideoMetadata(sha256 = "abcde")
      val validMetadata = VideoMetadata(sha256 = Hashing.sha256(videoData.bytes))

      "succeeds when the hashes match" in { f =>
        import f._

        val api = stub[ItvApi]
        (api.videoData(_: String)(_: ExecutionContext, _: Materializer)).when(*, *, *).returns(Future.successful(videoData))
        (api.videoMetadata(_: String)(_: ExecutionContext, _: Materializer)).when(*, *, *).returns(Future.successful(validMetadata))

        val eventualVideo = api.videoFileValidated("my-video-id")

        (api.videoData(_: String)(_: ExecutionContext, _: Materializer)).verify("my-video-id", ec, mat).once()
        (api.videoMetadata(_: String)(_: ExecutionContext, _: Materializer)).verify("my-video-id", ec, mat).once()
        eventualVideo.futureValue shouldBe videoData
      }

      "fails when the hashes don't match" in { f =>
        import f._

        val api = stub[ItvApi]
        (api.videoData(_: String)(_: ExecutionContext, _: Materializer)).when(*, *, *).returns(Future.successful(videoData))
        (api.videoMetadata(_: String)(_: ExecutionContext, _: Materializer)).when(*, *, *).returns(Future.successful(invalidMetadata))

        val eventualVideo = api.videoFileValidated("my-video-id")

        (api.videoData(_: String)(_: ExecutionContext, _: Materializer)).verify("my-video-id", ec, mat).once()
        (api.videoMetadata(_: String)(_: ExecutionContext, _: Materializer)).verify("my-video-id", ec, mat).once()
        eventualVideo.failed.futureValue shouldBe a[RuntimeException]
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

}
