import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "mindmap"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "batik" % "batik-transcoder" % "1.6-1"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(

      routesImport += "controllers.ViewSettings"

    )

    val importers = Project(id = "importers", base = file("importers")).dependsOn(main)
}
