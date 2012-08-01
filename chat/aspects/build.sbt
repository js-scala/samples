name := "chat-aspects"

libraryDependencies ++= Seq(
  "js-scala" %% "forest" % "0.3-SNAPSHOT"
)

sourceGenerators in Compile <+= (sourceDirectory in Compile, sourceManaged in Compile) map { (sourceDir, targetDir) =>
  forest.compiler.Compiler.compile(scalax.file.Path(sourceDir / "forest" / "Chat"), scalax.file.Path(targetDir), Seq("models._"), Seq("MessageOps", "ChatRoomOps"))
  (targetDir / "Chat.scala").get.map(_.getAbsoluteFile)
}

resolvers += Resolver.url("ivy-local", url("file://" + Path.userHome + "/.ivy2/local"))(Resolver.ivyStylePatterns)

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xexperimental", "-Yvirtualize")
