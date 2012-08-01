name := "drawing-play-jsgen"

organization := "js-scala"

version := "0.1-SNAPSHOT"

scalaVersion := Option(System.getenv("SCALA_VIRTUALIZED_VERSION")).getOrElse("2.10.0-M1-virtualized")

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xexperimental", "-Yvirtualize")

resolvers ++= Seq(
  ScalaToolsSnapshots
)

libraryDependencies ++= Seq(
  "EPFL" %% "js-scala" % "0.2-SNAPSHOT"
)
