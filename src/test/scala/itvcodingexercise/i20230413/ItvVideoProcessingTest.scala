package itvcodingexercise.i20230413

import akka.actor.ActorSystem
import akka.stream.Materializer
import itvcodingexercise.i20230413.RealItvApi.{VideoData, VideoMetadata}
import itvcodingexercise.i20230413.lib.{FileIO, Hashing}
import itvcodingexercise.i20230413.lib.media.FFmpeg
import itvcodingexercise.i20230413.testutils.{MultipleAssertions, SameThreadExecutionContext}
import org.scalamock.scalatest.MockFactory
import org.scalatest.Outcome
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.freespec.FixtureAnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

import java.io.File
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Using

final class ItvVideoProcessingTest extends FixtureAnyFreeSpecLike with MockFactory {
  "ItvVideoProcessing" - {
    ".makeThumbnail" - {
      "vanilla case" in { f =>
        import f._

        // Prepare data
        val videoData = VideoData(Seq(0x66, 0x67))
        val videoMetadata = VideoMetadata(sha256 = Hashing.sha256(videoData.bytes))
        val temporaryVideoFile = new File("temporary-video-file.mov")
        val videoAssetId = "my-asset-id"
        val thumbnailOffset = "10:10:55"
        val thumbnailFilePath = "../my-destination.png"
        val args = Seq(videoAssetId, thumbnailOffset, thumbnailFilePath)

        // Prepare dependencies
        val itvApi: ItvApi = stub[ItvApi]
        val fileIO: FileIO = stub[FileIO]
        val ffmpeg: FFmpeg = stub[FFmpeg]

        (itvApi.videoMetadata(_: String)(_: ExecutionContext, _: Materializer)).when(*, *, *).returns(Future.successful(videoMetadata))
        (itvApi.videoData(_: String)(_: ExecutionContext, _: Materializer)).when(*, *, *).returns(Future.successful(videoData))
        (fileIO.writeTemporaryFile _).when(*, *).returns(temporaryVideoFile)
        (ffmpeg.makeThumbnail _).when(*, *, *).returns(Future.successful(()))

        // Execute tested method
        val processor = new ItvVideoProcessing(itvApi, fileIO, ffmpeg)
        val eventualResult: Future[Unit] = processor.makeThumbnail(args)

        // Verify
        MultipleAssertions
          .assert { eventualResult.futureValue shouldBe () }
          .assert { (itvApi.videoMetadata(_: String)(_: ExecutionContext, _: Materializer)).verify(videoAssetId, ec, mat).once() }
          .assert { (itvApi.videoData(_: String)(_: ExecutionContext, _: Materializer)).verify(videoAssetId, ec, mat).once() }
          .assert { (fileIO.writeTemporaryFile _).verify(videoData.bytes, "downloaded.mov").once() }
          .assert { (ffmpeg.makeThumbnail _).verify(temporaryVideoFile, thumbnailOffset, new File(thumbnailFilePath)).once() }
          .verify()
      }

      "when given the wrong number of arguments" in { f =>
        import f._

        // Prepare data
        val args = Seq("my-asset-id", "10:10:55")

        // Prepare dependencies
        val itvApi: ItvApi = stub[ItvApi]
        val fileIO: FileIO = stub[FileIO]
        val ffmpeg: FFmpeg = stub[FFmpeg]

        // Execute tested method
        val processor = new ItvVideoProcessing(itvApi, fileIO, ffmpeg)
        val exception = the[Throwable] thrownBy (processor.makeThumbnail(args))

        // Verify
        MultipleAssertions
          .assert { exception shouldBe an[IllegalArgumentException] }
          .assert { exception.getMessage should include("arguments") }
          .assert { exception.getMessage should include("3") }
          .assert { exception.getMessage should include("2") }
          .assert { (itvApi.videoMetadata(_: String)(_: ExecutionContext, _: Materializer)).verify(*, *, *).never() }
          .assert { (itvApi.videoData(_: String)(_: ExecutionContext, _: Materializer)).verify(*, *, *).never() }
          .assert { (fileIO.writeTemporaryFile _).verify(*, *).never() }
          .assert { (ffmpeg.makeThumbnail _).verify(*, *, *).never() }
          .verify()
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
