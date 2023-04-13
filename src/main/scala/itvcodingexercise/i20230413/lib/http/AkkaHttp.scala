package itvcodingexercise.i20230413.lib.http

import akka.http.scaladsl.model.HttpRequest

import scala.concurrent.Future

/** A wrapper around Akka HTTP to make it easier to test. */
trait AkkaHttp {
  /** Makes a request and returns the response with a fully downloaded entity. */
  def strictRequest(request: HttpRequest): Future[StrictHttpResponse]
}
