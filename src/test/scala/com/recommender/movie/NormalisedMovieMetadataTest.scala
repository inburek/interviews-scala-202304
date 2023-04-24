package com.recommender.movie

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

import scala.util.Random

final class NormalisedMovieMetadataTest extends AnyFreeSpecLike {
  "NormalisedMovieMetadata" - {
    ".recommendMovie" - {
      "when there are no movies" in {
        implicit val random: Random = new Random(0)
        val normalisedMovieMetadata = NormalisedMovieMetadata(Map.empty)

        val recommendationResult = normalisedMovieMetadata.recommendMovie(MovieId("1"))

        recommendationResult shouldBe RecommendationResult(None, None)
      }

      "when there is only one movie" - {
        val metadata1 = MovieMetadata(MovieId("1"), MovieTitle("title1"), MovieLength(123), Seq(MovieTag("tag1")))
        val normalisedMovieMetadata = NormalisedMovieMetadata(Map(
          metadata1.id -> metadata1,
        ))
        "and we choose it" in {
          implicit val random: Random = new Random(0)

          val recommendationResult = normalisedMovieMetadata.recommendMovie(metadata1.id)

          recommendationResult shouldBe RecommendationResult(Some(metadata1), None)
        }

        "and we don't choose it" in {
          implicit val random: Random = new Random(0)

          val recommendationResult = normalisedMovieMetadata.recommendMovie(MovieId("123456789"))

          recommendationResult shouldBe RecommendationResult(None, None)
        }
      }

      "when there are two movies" - {
        "and they don't share a tag" in {
          implicit val random: Random = new Random(0)
          val metadata1 = MovieMetadata(MovieId("1"), MovieTitle("title1"), MovieLength(123), Seq(MovieTag("tag1")))
          val metadata2 = MovieMetadata(MovieId("2"), MovieTitle("title2"), MovieLength(456), Seq(MovieTag("tag2")))
          val normalisedMovieMetadata = NormalisedMovieMetadata(Map(
            metadata1.id -> metadata1,
            metadata2.id -> metadata2,
          ))

          val actual = (1 to 10).map(_ => normalisedMovieMetadata.recommendMovie(metadata1.id)).toSet
          val expected = Set(RecommendationResult(Some(metadata1), None))

          actual shouldBe expected
        }

        "and they share one tag of many" in {
          implicit val random: Random = new Random(0)
          val metadata1 = MovieMetadata(MovieId("1"), MovieTitle("title1"), MovieLength(123), Seq(MovieTag("tag11"), MovieTag("tag"), MovieTag("tag12")))
          val metadata2 = MovieMetadata(MovieId("2"), MovieTitle("title2"), MovieLength(456), Seq(MovieTag("tag21"), MovieTag("tag"), MovieTag("tag22")))
          val normalisedMovieMetadata = NormalisedMovieMetadata(Map(
            metadata1.id -> metadata1,
            metadata2.id -> metadata2,
          ))

          val actual = (1 to 10).map(_ => normalisedMovieMetadata.recommendMovie(metadata1.id)).toSet
          val expected = Set(RecommendationResult(Some(metadata1), Some(DefinitiveRecommendation(metadata2.id, MovieTag("tag")))))

          actual shouldBe expected
        }

        "and the chosen one has no tags" in {
          implicit val random: Random = new Random(0)
          val metadata1 = MovieMetadata(MovieId("1"), MovieTitle("title1"), MovieLength(123), Seq())
          val metadata2 = MovieMetadata(MovieId("2"), MovieTitle("title2"), MovieLength(456), Seq(MovieTag("tag21"), MovieTag("tag"), MovieTag("tag22")))
          val normalisedMovieMetadata = NormalisedMovieMetadata(Map(
            metadata1.id -> metadata1,
            metadata2.id -> metadata2,
          ))

          val actual = (1 to 10).map(_ => normalisedMovieMetadata.recommendMovie(metadata1.id)).toSet
          val expected = Set(RecommendationResult(Some(metadata1), None))

          actual shouldBe expected
        }
      }

      "when there are many movies" in {
        implicit val random: Random = new Random(0)
        val metadata1 = MovieMetadata(MovieId("1"), MovieTitle("title1"), MovieLength(123), Seq(MovieTag("tag11"), MovieTag("tag"), MovieTag("tag12")))
        val metadata2 = MovieMetadata(MovieId("2"), MovieTitle("title2"), MovieLength(456), Seq(MovieTag("tag21"), MovieTag("tag"), MovieTag("tag22")))
        val metadata3 = MovieMetadata(MovieId("3"), MovieTitle("title3"), MovieLength(789), Seq(MovieTag("tag31"), MovieTag("tag"), MovieTag("tag32")))
        val metadata4 = MovieMetadata(MovieId("4"), MovieTitle("unchosen"), MovieLength(357), Seq(MovieTag("tag41"), MovieTag("tag42"), MovieTag("tag43")))

        val normalisedMovieMetadata = NormalisedMovieMetadata(Map(
          metadata1.id -> metadata1,
          metadata2.id -> metadata2,
          metadata3.id -> metadata3,
          metadata4.id -> metadata4,
        ))

        val actual = (1 to 10).map(_ => normalisedMovieMetadata.recommendMovie(metadata1.id)).toSet
        val expected = Set(
          RecommendationResult(Some(metadata1), Some(DefinitiveRecommendation(metadata2.id, MovieTag("tag")))),
          RecommendationResult(Some(metadata1), Some(DefinitiveRecommendation(metadata3.id, MovieTag("tag")))),
        )

        actual shouldBe expected
      }
    }
  }
}
