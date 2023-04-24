package com.recommender.movie

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._
import spray.json._

final class MovieMetadataTest extends AnyFreeSpecLike {
  "MovieMetadata" - {
    ".jsonFormat" - {
      val obj = MovieMetadata(MovieId("1"), MovieTitle("title"), MovieLength(1), Seq(MovieTag("tag1"), MovieTag("tag2")))
      val json = """{"id":"1","title":"title","length":1,"tags":["tag1", "tag2"]}""".parseJson

      "serializes correctly" in {
        obj.toJson shouldBe json
      }
      "deserializes correctly" in {
        json.convertTo[MovieMetadata] shouldBe obj
      }
    }
  }
}
