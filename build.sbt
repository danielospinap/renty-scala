enablePlugins(JavaAppPackaging)
name := "Renty"

version := "0.1"

scalaVersion := "2.12.7"

val circeVersion = "0.10.0"

libraryDependencies ++= Seq(
  "com.twitter"%%"finagle-http"%"6.41.0",
  //"com.google.firebase" % "firebase-admin" % "6.0.0",
  //"com.typesafe.akka" %%  "akka-http-core"    % "2.4.5",
  "com.google.firebase"%"firebase-server-sdk"%"3.0.0",
   "com.github.firebase4s" %% "firebase4s" % "0.0.4",
  "com.typesafe.akka" %% "akka-actor" % "2.5.18",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.18" % Test,
  "com.typesafe.akka" %% "akka-stream" % "2.5.18",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.18" % Test,
  "com.typesafe.akka" %% "akka-http" % "10.1.5",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5" % Test,

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.22.0",

  "org.mongodb.scala" %% "mongo-scala-driver" % "2.5.0",

  "ch.qos.logback" % "logback-classic" % "1.2.3",

  //test libraries
  "org.mongodb" % "mongo-java-driver" % "3.4.2",
  "com.github.fakemongo" % "fongo" % "2.1.0" % "test"
)

