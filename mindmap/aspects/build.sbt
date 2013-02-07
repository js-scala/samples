name := "mindmap-aspects"

libraryDependencies ++= Seq(
  "js-scala" %% "forest" % "0.4-SNAPSHOT",
  "EPFL" %% "js-scala" % "0.3-SNAPSHOT",
  "EPFL" %% "lms" % "0.3-SNAPSHOT"
)

sourceGenerators in Compile <+= (sourceDirectory in Compile, sourceManaged in Compile) map { (sourceDir, targetDir) =>
  forest.compiler.Compiler.compile(
      scalax.file.Path(sourceDir / "forest" / "Templates"),
      scalax.file.Path(targetDir),
      Seq("models._", "scala.xml.Node", "scala.virtualization.lms.common._"),
      Seq("Models", "ScalaOpsPkg", "LiftScala")
  )
  (targetDir / "Templates.scala").get.map(_.getAbsoluteFile)
}

scalaOrganization := "org.scala-lang.virtualized"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xexperimental", "-Yvirtualize")
