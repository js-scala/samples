name := "mindmap-aspects"

libraryDependencies ++= Seq(
  "js-scala" %% "forest" % "0.5-SNAPSHOT",
  "EPFL" %% "js-scala" % "0.4-SNAPSHOT",
  "EPFL" %% "lms" % "0.3-SNAPSHOT"
)

scalaOrganization := "org.scala-lang.virtualized"

scalaVersion := "2.10.2-RC1"

resolvers += Resolver.sonatypeRepo("snapshots")

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xexperimental", "-Yvirtualize")
