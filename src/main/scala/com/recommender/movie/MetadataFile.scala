package com.recommender.movie

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

final case class MetadataFile(metadatas: Seq[MovieMetadata]) {
  lazy val normalised: NormalisedMovieMetadata = {
    new NormalisedMovieMetadata(metadatas.map(metadata => metadata.id -> metadata).toMap) // Assume uniqueness.
  }
}

object MetadataFile {
  implicit val metadataFileJsonFormat: RootJsonFormat[MetadataFile] = jsonFormat(MetadataFile.apply, "metadatas")
}
