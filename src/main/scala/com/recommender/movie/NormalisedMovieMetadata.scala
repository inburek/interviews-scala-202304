package com.recommender.movie

import com.recommender.utils.RandomOps.RichRandom

import scala.util.Random

final case class NormalisedMovieMetadata(metadatasById: Map[MovieId, MovieMetadata]) {
  private lazy val moviesByTag: Map[MovieTag, Seq[MovieId]] = {
    metadatasById.values
      .flatMap(metadata => metadata.tags.map(tag => tag -> metadata.id))
      .groupBy(_._1: MovieTag)
      .view
      .mapValues(_.map(_._2: MovieId).toVector.distinct)
      .toMap
  }

  def recommendMovie(movieId: MovieId)(implicit random: Random): RecommendationResult = {
    val maybeMetadata = metadatasById.get(movieId)
    val maybeRecommendation: Option[DefinitiveRecommendation] = for {
      metadata <- maybeMetadata
      matchingMovieIds =
        metadata.tags
          .flatMap(tag => moviesByTag.getOrElse(tag, Seq.empty).map(id => (id, tag)))
          .distinct
          .filterNot(recommendation => (recommendation._1: MovieId) == movieId)

      (recommendedMovieId, sharedTag) <- random.choose(matchingMovieIds)
    } yield {
      DefinitiveRecommendation(recommendedMovieId, sharedTag)
    }

    RecommendationResult(maybeMetadata, maybeRecommendation)
  }
}
