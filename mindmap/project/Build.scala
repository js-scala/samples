import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "mindmap"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "batik" % "batik-transcoder" % "1.6-1"
    )

    val aspects = Project(id = "aspects", base = file("aspects"))

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(

      scalaOrganization := "org.scala-lang.virtualized",

      scalaVersion := "2.10.0-M7",

      routesImport += "controllers.ViewSettings"

    )

    val importers = Project(id = "importers", base = file("importers")).dependsOn(main)
}
