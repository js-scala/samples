name := "chat-core"

libraryDependencies ++= Seq(
  "js-scala" %% "forest" % "0.3-SNAPSHOT",
  "play" %% "play" % "2.0.2-virtualized"
)

resolvers += Resolver.url("ivy-local", url("file://" + Path.userHome + "/.ivy2/local"))(Resolver.ivyStylePatterns)

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xexperimental", "-Yvirtualize")
