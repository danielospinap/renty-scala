enablePlugins(JavaAppPackaging)
name := "renty-scala-feature-SearchChanges"


version := "0.1"

scalaVersion := "2.12.7"

resolvers += Resolver.jcenterRepo

val akkaHttp = "10.1.1"
val akka = "2.5.11"
val circe = "0.9.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttp,
  "com.typesafe.akka" %% "akka-stream" % akka,
  //"com.typesafe.akka" %% "akka-stream-alpakka-google-fcm" % "1.0-M1",
  //"com.elkozmon" %% "akka-scaladsl" % "2.1" ,
  "com.elkozmon" %% "akka-stream-firebase-queue" % "2.1" ,
  "com.google.firebase" % "firebase-admin" % "5.9.0" ,
  //"com.elkozmon.akka" % "firebasescaladsl" % "5.9.0" ,



  "com.typesafe.akka" %% "akka-slf4j" % akka,

  "de.heikoseeberger" %% "akka-http-circe" % "1.20.1",

  "io.circe" %% "circe-generic" % circe,

  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0",

  //test libraries
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.pegdown" % "pegdown" % "1.6.0" % "test",
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttp % "test",
  "org.mongodb" % "mongo-java-driver" % "3.4.2",
  "com.github.fakemongo" % "fongo" % "2.1.0" % "test",
  "org.scalactic" %% "scalactic" % "3.0.0",
  
)

testOptions in Test ++= Seq(
  Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports"),
  Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")
)


pomExtra :=
  <scm>
    <url>git@github.com:elkozmon/akka-stream-firebase-queue.git</url>
    <connection>scm:git:git@github.com:elkozmon/akka-stream-firebase-queue.git</connection>
  </scm>
  <developers>
    <developer>
      <id>elkozmon</id>
      <name>Lubos Kozmon</name>
      <timezone>+1</timezone>
      <url>https://elkozmon.com</url>
    </developer>
  </developers>
