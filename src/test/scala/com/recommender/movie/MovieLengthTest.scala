package com.recommender.movie

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import spray.json.DefaultJsonProtocol._
import spray.json._

final class MovieLengthTest extends AnyFreeSpecLike {
  "MovieLength" - {
    ".jsonFormat" - {
      "serializes correctly" in {
        val movieLength = MovieLength(123)
        val json = movieLength.toJson
        json shouldBe 123.toJson
      }
      "deserializes correctly" in {
        val json = 123.toJson
        val movieLength = json.convertTo[MovieLength]
        movieLength shouldBe MovieLength(123)
      }
    }
  }
}
