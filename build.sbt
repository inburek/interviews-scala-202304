import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val mainDependencies = Seq(
  "org.typelevel" %% "cats-core" % "2.9.0",
)
val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.2.15" % Test,
)

lazy val root = (project in file("."))
  .settings(
    name := "ct-coding-exercise",
    libraryDependencies ++= mainDependencies ++ testDependencies
  )
