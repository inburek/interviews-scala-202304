package com.recommender.movie

import spray.json.DefaultJsonProtocol._
import spray.json._

final case class RecommendationResult(maybeMetadata: Option[MovieMetadata], maybeRecommendation: Option[DefinitiveRecommendation])
object RecommendationResult {
  implicit val recommendationJsonFormat: RootJsonFormat[RecommendationResult] = jsonFormat2(RecommendationResult.apply)
}
