libraryDependencies += "forest" %% "compiler" % "0.1-SNAPSHOT"

resolvers += Resolver.url("ivy-local", url("file://" + Path.userHome + "/.ivy2/local"))(Resolver.ivyStylePatterns)