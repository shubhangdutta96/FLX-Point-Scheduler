name := "flxPoint"

version := "0.1"

scalaVersion := "2.13.10"
val akkaVersion = "2.8.2"
val akkaHttpVersion = "10.5.2"

fork := true


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
  "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "mysql" % "mysql-connector-java" % "8.0.33",
  "com.typesafe.slick" %% "slick" % "3.4.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1",
  "org.postgresql" % "postgresql" % "42.6.0",
  "org.scala-lang.modules" %%  "scala-xml" % "2.2.0",
  "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-xml" % "2.15.2",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.15.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.15.2",
  "org.jsoup" % "jsoup" % "1.14.3",
  "org.json4s" %% "json4s-native" % "4.0.3",
  "commons-logging" % "commons-logging" % "1.3.1"
)

Compile / mainClass := Some("com.ebctech.web.control.Boot")

//assembly / assemblyMergeStrategy := {
//  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//  case PathList("reference.conf") => MergeStrategy.concat
//  case x => MergeStrategy.first
//}
//enablePlugins(DockerPlugin)
//enablePlugins(JavaAppPackaging)
//assembly / assemblyJarName := "flxPoint.jar"
