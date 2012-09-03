import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "mindmap"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "batik" % "batik-transcoder" % "1.6-1"
    )

    val core = Project(id = "core", base = file("core"))
    val aspects = Project(id = "aspects", base = file("aspects")).dependsOn(core)

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(

      resolvers += Resolver.url("ivy-local", url("file://" + Path.userHome + "/.ivy2/local"))(Resolver.ivyStylePatterns),

      scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xexperimental", "-Yvirtualize"),

      routesImport += "controllers.ViewSettings"

    ).dependsOn(core)

    val importers = Project(id = "importers", base = file("importers")).dependsOn(main)
}
