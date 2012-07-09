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

      resolvers += Resolver.url("ivy-local", url("file://" + Path.userHome + "/.ivy2/local"))(Resolver.ivyStylePatterns),

      scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xexperimental", "-Yvirtualize"),

      sourceGenerators in Compile <+= (sourceDirectory in Compile, sourceManaged in Compile) map { (sourceDir, targetDir) =>
        forest.compiler.Compiler.compile(sourceDir / "views" / "Chat", targetDir, Seq("models._"), Seq("MessageOps", "ChatRoomOps"))
        (targetDir ** "*.scala").get.map(_.getAbsoluteFile)
      }

    )

}
