name := "discovery-personalisation-candidate-test"

version := "0.1"

scalaVersion := "2.13.7"

val akkaVersion = "2.8.0"
val akkaHttpVersion = "10.5.1"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.9.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test

// HTTP
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http" % akkaHttpVersion

// JSON
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.6"

// Logging
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.6" % Runtime
