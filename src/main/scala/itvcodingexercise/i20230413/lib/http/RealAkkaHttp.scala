package itvcodingexercise.i20230413.lib.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest

import scala.concurrent.Future
import scala.concurrent.duration.{DurationInt, FiniteDuration}

/** A real implementation of [[AkkaHttp]], making real HTTP requests. */
final class RealAkkaHttp(entityTimeout: FiniteDuration)
                        (implicit val system: ActorSystem) extends AkkaHttp {

  private val http = Http()

  def strictRequest(request: HttpRequest): Future[StrictHttpResponse] = {
    http.singleRequest(request).flatMap(StrictHttpResponse(_, entityTimeout))
  }

}

object RealAkkaHttp {
  def apply()(implicit system: ActorSystem): RealAkkaHttp = new RealAkkaHttp(entityTimeout = 60.seconds)
}
