import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "chat"
    val appVersion      = "1.0-SNAPSHOT"


    val appDependencies = Seq(
      "forest" %% "forest" % "0.1-SNAPSHOT"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(

      resolvers ++= Seq(ScalaToolsSnapshots, Resolver.file("yop", file("/Users/julienrichard-foy/.ivy2/local"))(Resolver.ivyStylePatterns)),

      scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xexperimental", "-Yvirtualize"),

      sourceGenerators in Compile <+= (sourceDirectory in Compile, sourceManaged in Compile) map { (sourceDir, targetDir) =>
        forest.compiler.Compiler.compile(sourceDir / "forest" / "views" / "Form", targetDir, Seq(), Seq())
        (targetDir ** "*.scala").get.map(_.getAbsoluteFile)
      }

    )

}
