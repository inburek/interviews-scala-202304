package com.recommender.movie

import com.recommender.utils.PlainWrapperJsonFormat
import spray.json.DefaultJsonProtocol._
import spray.json.JsonFormat

final case class MovieId(value: String) extends AnyVal
object MovieId {
  implicit val jsonFormat: JsonFormat[MovieId] = new PlainWrapperJsonFormat(apply, _.value)
}

final case class MovieTitle(value: String) extends AnyVal
object MovieTitle {
  implicit val jsonFormat: JsonFormat[MovieTitle] = new PlainWrapperJsonFormat(apply, _.value)
}

final case class MovieLength(value: Int) extends AnyVal
object MovieLength {
  implicit val jsonFormat: JsonFormat[MovieLength] = new PlainWrapperJsonFormat(apply, _.value)
}

final case class MovieTag(value: String) extends AnyVal
object MovieTag {
  implicit val jsonFormat: JsonFormat[MovieTag] = new PlainWrapperJsonFormat(apply, _.value)
}
