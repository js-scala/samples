import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "chat"
    val appVersion      = "1.0-SNAPSHOT"


    val main = play.Project(appName, appVersion).settings(

      resolvers += Resolver.url("ivy-local", url("file://" + Path.userHome + "/.ivy2/local"))(Resolver.ivyStylePatterns),

      scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),

      libraryDependencies ++= Seq(
        "js-scala" %% "forest" % "0.4-SNAPSHOT",
        "org.fusesource.scalate" %% "scalate-core" % "1.6.1"
      )

    )

}
