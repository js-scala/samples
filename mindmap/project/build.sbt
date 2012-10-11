libraryDependencies += "js-scala" % "forest-compiler_2.9.1" % "0.3-SNAPSHOT"

resolvers += Resolver.file("ivy-local", file(Path.userHome + "/.ivy2/local"))(Resolver.ivyStylePatterns)