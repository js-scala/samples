libraryDependencies += "forest" % "compiler_2.9.1" % "0.3-SNAPSHOT"

resolvers += Resolver.url("ivy-local", url("file://" + Path.userHome + "/.ivy2/local"))(Resolver.ivyStylePatterns)