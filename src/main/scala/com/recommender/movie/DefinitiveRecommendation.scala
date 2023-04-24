package com.recommender.movie

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

final case class DefinitiveRecommendation(movieId: MovieId, sharedTag: MovieTag)

object DefinitiveRecommendation {
  implicit val definitiveRecommendationJsonFormat: RootJsonFormat[DefinitiveRecommendation] = jsonFormat2(DefinitiveRecommendation.apply)
}
