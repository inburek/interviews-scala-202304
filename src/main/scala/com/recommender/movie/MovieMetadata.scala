package com.recommender.movie

import spray.json.DefaultJsonProtocol._
import spray.json.JsonFormat

final case class MovieMetadata(id: MovieId, title: MovieTitle, length: MovieLength, tags: Seq[MovieTag])
object MovieMetadata {
  implicit val jsonFormat: JsonFormat[MovieMetadata] = jsonFormat4(MovieMetadata.apply)
}
