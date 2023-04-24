package com.recommender.movie

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._
import spray.json._

final class RecommendationResultTest extends AnyFreeSpecLike {
  "RecommendationResult" - {
    ".recommendationJsonFormat" - {

      "with both recommendation and metadata" - {
        val obj = RecommendationResult(
          Some(MovieMetadata(MovieId("1"), MovieTitle("title1"), MovieLength(123), Seq(MovieTag("tag1")))),
          Some(DefinitiveRecommendation(MovieId("1"), MovieTag("tag1")))
        )

        val json =
          """{
               "maybeMetadata": {
                 "id": "1",
                 "title": "title1",
                 "length": 123,
                 "tags": [
                   "tag1"
                 ]
               },
               "maybeRecommendation": {
                 "movieId": "1",
                 "sharedTag": "tag1"
               }
             }
          """.stripMargin.parseJson

        "serializes correctly" in {
          obj.toJson shouldBe json
        }
        "deserializes correctly" in {
          json.convertTo[RecommendationResult] shouldBe obj
        }
      }

      "with neither recommendations or metadata" - {
        val obj = RecommendationResult(None, None)

        val json = """{}""".parseJson

        "serializes correctly" in {
          obj.toJson shouldBe json
        }
        "deserializes correctly" in {
          json.convertTo[RecommendationResult] shouldBe obj
        }
      }
    }
  }
}
