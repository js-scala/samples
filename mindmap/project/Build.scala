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

      resolvers += Resolver.file("ivy-local", file(Path.userHome + "/.ivy2/local"))(Resolver.ivyStylePatterns),

      routesImport += "controllers.ViewSettings"

    )

    val importers = Project(id = "importers", base = file("importers")).dependsOn(main)
}
