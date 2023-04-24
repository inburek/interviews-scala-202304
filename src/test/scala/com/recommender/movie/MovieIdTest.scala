package com.recommender.movie

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import spray.json.DefaultJsonProtocol._
import spray.json._

final class MovieIdTest extends AnyFreeSpecLike {
  "MovieId" - {
    ".jsonFormat" - {
      "serializes correctly" in {
        val movieId = MovieId("123")
        val json = movieId.toJson
        json shouldBe "123".toJson
      }
      "deserializes correctly" in {
        val json = "123".toJson
        val movieId = json.convertTo[MovieId]
        movieId shouldBe MovieId("123")
      }
    }
  }
}
