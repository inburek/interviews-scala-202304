package itvcodingexercise.i20230413.lib.http

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, StatusCodes, Uri}
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

/** Real network test to check that [[RealAkkaHttp]] isn't doing something silly.
  *
  * This really shouldn't go into this project's "unit tests" because it has external dependencies,
  *   so it isn't a unit test. But we've only got one module to work with, so we'll keep it here for now.
  *
  * It should, however, run *somewhere* when the tested class or its dependencies change.
  */
final class RealAkkaHttpNetworkTest extends AnyFreeSpecLike {
  "RealAkkaHttp" - {
    "can connect to example.com and download the entire request" in {
      implicit val system: ActorSystem = akka.actor.ActorSystem("manual-http-testing-actor-system")
      val akkaHttp: AkkaHttp = RealAkkaHttp()

      val request = HttpRequest(HttpMethods.GET, Uri("https://example.com"))
      val responseFuture: Future[StrictHttpResponse] = akkaHttp.strictRequest(request)

      val response: StrictHttpResponse = Await.result(responseFuture, 10.seconds)
      response.strictResponse.status shouldBe StatusCodes.OK
      response.strictResponse.entity shouldBe response.entity
      response.entity.data.utf8String should include("</html>") // If we have the end tag, we must have downloaded everything.
    }
  }
}
