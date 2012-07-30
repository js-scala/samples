name := "chat-core"

libraryDependencies ++= Seq(
  "forest" %% "forest" % "0.3-SNAPSHOT",
  "play" %% "play" % "2.0-SNAPSHOT"
)

resolvers += Resolver.url("ivy-local", url("file://" + Path.userHome + "/.ivy2/local"))(Resolver.ivyStylePatterns)

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xexperimental", "-Yvirtualize")
