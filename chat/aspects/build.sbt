name := "chat-aspects"

scalaVersion := "2.10.0"

scalaOrganization := "org.scala-lang.virtualized"

libraryDependencies ++= Seq(
  "js-scala" %% "forest" % "0.4-SNAPSHOT"
)

sourceGenerators in Compile <+= (sourceDirectory in Compile, sourceManaged in Compile) map { (sourceDir, targetDir) =>
  forest.compiler.Compiler.compile(
    scalax.file.Path(sourceDir / "forest" / "Chat"),
    scalax.file.Path(targetDir),
    Seq("aspects._", "scala.virtualization.lms.common._", "scala.js._"),
    Seq("Models", "LiftJsScala")
  )
  (targetDir / "Chat.scala").get.map(_.getAbsoluteFile)
}

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xexperimental", "-Yvirtualize")
