package itvcodingexercise.i20230413.lib.http

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpEntity, HttpResponse}

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

/** Wraps around a [[HttpResponse]] and its [[HttpEntity.Strict]] entity.
  * Guarantees that the entity is fully downloaded.
  */
sealed abstract case class StrictHttpResponse(strictResponse: HttpResponse, entity: HttpEntity.Strict)

object StrictHttpResponse {
  def apply(nonStrictResponse: HttpResponse, downloadTimeout: FiniteDuration)
           (implicit ec: ExecutionContext, sys: ActorSystem): Future[StrictHttpResponse] = {

    nonStrictResponse.entity
      .toStrict(downloadTimeout)
      .map { strictEntity: HttpEntity.Strict =>
        val strictResponse = nonStrictResponse.withEntity(entity = strictEntity)
        new StrictHttpResponse(strictResponse, strictEntity) {}
      }
  }
}
