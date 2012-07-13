name := "drawing"

organization := "EPFL"

version := "0.2-SNAPSHOT"

scalaVersion := Option(System.getenv("SCALA_VIRTUALIZED_VERSION")).getOrElse("2.10.0-M1-virtualized")

//--- Dependencies

libraryDependencies ++= Seq(
    "EPFL" %% "js-scala" % "0.2-SNAPSHOT",
    "org.eclipse.jetty" % "jetty-webapp" % "8.0.1.v20110908" % "container",
    "org.eclipse.jetty" % "jetty-websocket" % "8.0.1.v20110908",
    "org.eclipse.jetty" % "jetty-servlet" % "8.0.1.v20110908",
    "javax.servlet" % "servlet-api" % "2.5" % "provided->default")
    
//--- End of Dependencies

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xexperimental", "-Yvirtualize")

// import web plugin keys
seq(webSettings :_*)
