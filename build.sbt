import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val akkaVersion = "2.8.0"
val akkaHttpVersion = "10.5.0"

val mainDependencies = Seq(
  "org.typelevel" %% "cats-core" % "2.9.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "ch.qos.logback" % "logback-classic" % "1.4.6" % Runtime,

  // Akka HTTP documentation says we need to depend on Akka itself too.
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,

  "io.spray" %% "spray-json" % "1.3.6",
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
)
val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.2.15" % Test,
  "org.scalamock" %% "scalamock" % "5.2.0" % Test,
)

lazy val root = (project in file("."))
  .settings(
    name := "ct-coding-exercise",
    libraryDependencies ++= mainDependencies ++ testDependencies
  )
