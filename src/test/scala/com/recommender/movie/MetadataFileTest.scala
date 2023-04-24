package com.recommender.movie

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._
import spray.json._

final class MetadataFileTest extends AnyFreeSpecLike {
  "MetadataFile" - {
    ".normalised" - {
      "for an empty file" in {
        MetadataFile(Seq.empty).normalised shouldBe NormalisedMovieMetadata(Map.empty)
      }

      "for a file with one metadata element" in {
        val metadata1 = MovieMetadata(MovieId("1"), MovieTitle("Title 1"), MovieLength(1), Seq(MovieTag("tag1"), MovieTag("tag2")))
        val expected = NormalisedMovieMetadata(Map(
          metadata1.id -> metadata1
        ))

        MetadataFile(Seq(metadata1)).normalised shouldBe expected
      }

      "for a file with multiple distinct metadata elements" in {
        val metadata1 = MovieMetadata(MovieId("1"), MovieTitle("Title 1"), MovieLength(1), Seq(MovieTag("tag1"), MovieTag("tag2")))
        val metadata2 = MovieMetadata(MovieId("2"), MovieTitle("Title 2"), MovieLength(52), Seq(MovieTag("tag1")))
        val expected = NormalisedMovieMetadata(Map(
          metadata1.id -> metadata1,
          metadata2.id -> metadata2
        ))

        MetadataFile(Seq(metadata1, metadata2)).normalised shouldBe expected
      }

      "for a file with multiple duplicate metadata elements" in {
        val metadata1 = MovieMetadata(MovieId("1"), MovieTitle("Title 1"), MovieLength(1), Seq(MovieTag("tag1"), MovieTag("tag2")))
        val metadata2 = MovieMetadata(MovieId("1"), MovieTitle("Title 2"), MovieLength(52), Seq(MovieTag("tag1")))
        val expected = NormalisedMovieMetadata(Map(
          metadata2.id -> metadata2 // Second one wins because of toMap.
        ))

        MetadataFile(Seq(metadata1, metadata2)).normalised shouldBe expected
      }
    }

    ".metadataFileJsonFormat" - {
      val obj = MetadataFile(Seq(
        MovieMetadata(MovieId("1"), MovieTitle("Title 1"), MovieLength(1), Seq(MovieTag("tag1"), MovieTag("tag2"))),
        MovieMetadata(MovieId("2"), MovieTitle("Title 2"), MovieLength(52), Seq(MovieTag("tag1"))),
      ))
      val json =
        """{
             "metadatas":[
               { "id": "1", "title": "Title 1", "length": 1,  "tags": ["tag1","tag2"] },
               { "id": "2", "title": "Title 2", "length": 52, "tags": ["tag1"] }
             ]
           }
        """.stripMargin.parseJson

      "serializes correctly" in {
        obj.toJson shouldBe json
      }
      "deserializes correctly" in {
        json.convertTo[MetadataFile] shouldBe obj
      }
    }
  }
}
