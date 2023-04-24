package com.recommender.movie

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._
import spray.json._

final class DefinitiveRecommendationTest extends AnyFreeSpecLike {
  "DefinitiveRecommendation" - {
    ".definitiveRecommendationJsonFormat" - {
      val obj = DefinitiveRecommendation(MovieId("1"), MovieTag("tag1"))

      val json =
        """{
             "movieId": "1",
             "sharedTag": "tag1"
           }
        """.stripMargin.parseJson

      "serializes correctly" in {
        obj.toJson shouldBe json
      }
      "deserializes correctly" in {
        json.convertTo[DefinitiveRecommendation] shouldBe obj
      }
    }
  }
}
