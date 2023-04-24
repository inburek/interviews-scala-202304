package com.recommender.movie

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import spray.json.DefaultJsonProtocol._
import spray.json._

final class MovieTitleTest extends AnyFreeSpecLike {
  "MovieTitle" - {
    ".jsonFormat" - {
      "serializes correctly" in {
        val movieTitle = MovieTitle("Hello World")
        val json = movieTitle.toJson
        json shouldBe "Hello World".toJson
      }
      "deserializes correctly" in {
        val json = "Hello World".toJson
        val movieTitle = json.convertTo[MovieTitle]
        movieTitle shouldBe MovieTitle("Hello World")
      }
    }
  }
}
