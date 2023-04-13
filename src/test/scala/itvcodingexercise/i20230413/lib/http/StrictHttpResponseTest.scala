package itvcodingexercise.i20230413.lib.http

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.stream.scaladsl.Source
import akka.util.ByteString
import itvcodingexercise.i20230413.testutils.SameThreadExecutionContext
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

import java.util.concurrent.TimeoutException
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

final class StrictHttpResponseTest extends AnyFreeSpecLike {
  "StrictHttpResponse" - {
    ".apply" - {
      implicit val ec: ExecutionContext = SameThreadExecutionContext
      implicit val as: ActorSystem = ActorSystem("test-execution-context", defaultExecutionContext = Some(ec))

      val strictEntity = HttpEntity.Strict(ContentTypes.`text/plain(UTF-8)`, ByteString("Hello, world!"))

      "returns immediately if the entity is already strict" in {
        val response = HttpResponse(entity = strictEntity)

        val eventualStrictResponse = StrictHttpResponse(response, downloadTimeout = 0.seconds)

        val StrictHttpResponse(actualResponse, actualEntity) = eventualStrictResponse.futureValue
        actualResponse shouldBe response
        actualEntity shouldBe strictEntity
      }

      "completes the future when the entity is downloaded" in {
        val eventualEntity = HttpEntity.Default(strictEntity.contentType, strictEntity.contentLength, Source.single(strictEntity.data))
        val response = HttpResponse(entity = eventualEntity)

        val eventualStrictResponse = StrictHttpResponse(response, downloadTimeout = 1.seconds)

        val StrictHttpResponse(actualResponse, actualEntity) = eventualStrictResponse.futureValue
        actualResponse shouldBe response.withEntity(strictEntity)
        actualEntity shouldBe strictEntity
      }

      "fails the future if the entity download times out" in {
        val eventualEntity = HttpEntity.Default(strictEntity.contentType, strictEntity.contentLength, Source.never)
        val response = HttpResponse(entity = eventualEntity)

        // Debatable how much we're testing timeouts with 0 seconds.
        val eventualStrictResponse = StrictHttpResponse(response, downloadTimeout = 0.seconds)

        eventualStrictResponse.failed.futureValue shouldBe a[TimeoutException]
      }

      "fails the future if the entity download fails" in {
        val exception = new RuntimeException("Stream deliberately failed for testing purposes.")
        val eventualEntity = HttpEntity.Default(strictEntity.contentType, strictEntity.contentLength, Source.failed(exception))
        val response = HttpResponse(entity = eventualEntity)

        // Debatable how much we're testing timeouts with 0 seconds.
        val eventualStrictResponse = StrictHttpResponse(response, downloadTimeout = 0.seconds)

        eventualStrictResponse.failed.futureValue shouldBe exception
      }
    }
  }

}
