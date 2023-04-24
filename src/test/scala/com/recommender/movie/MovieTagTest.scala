package com.recommender.movie

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import spray.json.DefaultJsonProtocol._
import spray.json._

final class MovieTagTest extends AnyFreeSpecLike {
  "MovieTag" - {
    ".jsonFormat" - {
      "serializes correctly" in {
        val movieTag = MovieTag("Crime")
        val json = movieTag.toJson
        json shouldBe "Crime".toJson
      }
      "deserializes correctly" in {
        val json = "Crime".toJson
        val movieTag = json.convertTo[MovieTag]
        movieTag shouldBe MovieTag("Crime")
      }
    }
  }
}
