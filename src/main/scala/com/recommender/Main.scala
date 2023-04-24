package com.recommender

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.Uri.{Authority, Host}
import akka.http.scaladsl.server.Directives._
import com.recommender.movie.{MetadataFile, MovieId}
import com.typesafe.scalalogging.LazyLogging
import spray.json._

import java.nio.file.{Files, Path}
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext}
import scala.jdk.CollectionConverters._
import scala.util.Random

object Main extends App with LazyLogging {
  private val authority = Authority(Host("localhost"), 8080)

  private implicit val random: Random = new Random()
  private implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  private implicit val executionContext: ExecutionContext = system.executionContext

  private val metadatas: MetadataFile = {
    Files
      .readAllLines(Path.of("src/main/resources/metadatas.json"))
      .asScala
      .mkString("\n")
      .parseJson
      .convertTo[MetadataFile]
  }

  private val route = {
    concat(
      path("metadatas") {
        get {
          complete {
            metadatas
          }
        }
      },
      path("recommendations") {
        parameters("movieId") { movieIdString =>
          val movieId = MovieId(movieIdString)
          get {
            complete {
              metadatas.normalised.recommendMovie(movieId)
            }
          }
        }
      },
    )
  }

  private val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
  Await.result(bindingFuture, 20.seconds)

  logger.info(s"Server now online. Please navigate to http://${authority.normalizedForHttp(encrypted = false)}/recommendations?movieId=5")
}
