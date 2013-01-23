name := "drawing-play-jsgen"

organization := "js-scala"

version := "0.1-SNAPSHOT"

scalaOrganization := "org.scala-lang.virtualized"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xexperimental", "-Yvirtualize")

libraryDependencies ++= Seq(
  "EPFL" %% "js-scala" % "0.3-SNAPSHOT"
)
