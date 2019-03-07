enablePlugins(JavaAppPackaging)
name := "Renty"

version := "0.1"

scalaVersion := "2.12.7"

val circeVersion = "0.10.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.18",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.18" % Test,
  "com.typesafe.akka" %% "akka-stream" % "2.5.18",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.18" % Test,
  "com.typesafe.akka" %% "akka-http" % "10.1.5",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5" % Test,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.spray" %%  "spray-json" % "1.3.5",
  "de.heikoseeberger" %% "akka-http-circe" % "1.22.0",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.5.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.github.firebase4s" %% "firebase4s" % "0.0.4",
  //test libraries
  "org.mongodb" % "mongo-java-driver" % "3.0.0",
  "org.mongodb" % "mongodb-driver-core" % "3.0.0",
  "com.github.fakemongo" % "fongo" % "2.1.0" % "test",
  "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test",
  "de.bwaldvogel" % "mongo-java-server" % "1.13.0" % "test",
  "com.github.simplyscala" %% "scalatest-embedmongo" % "0.2.4" % "test"
)

