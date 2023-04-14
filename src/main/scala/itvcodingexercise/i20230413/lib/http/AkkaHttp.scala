package itvcodingexercise.i20230413.lib.http

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.{FromResponseUnmarshaller, Unmarshal}
import akka.stream.Materializer

import scala.concurrent.{ExecutionContext, Future}

/** A wrapper around Akka HTTP to make it easier to test. */
trait AkkaHttp {
  protected implicit final lazy val mat: Materializer = Materializer(system)

  /** Provides us with an actor system so we can abstract away utility methods. */
  def system: ActorSystem

  /** Makes a request and returns the response with a fully downloaded entity. */
  def strictRequest(request: HttpRequest): Future[StrictHttpResponse]

  implicit final def executionContext: ExecutionContext = system.dispatcher

  final def singleStrictRequest[Response: FromResponseUnmarshaller](request: HttpRequest): Future[Response] = {
    strictRequest(request).flatMap(shr => Unmarshal(shr.strictResponse).to[Response])
  }
}
