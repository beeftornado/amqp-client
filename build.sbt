name := "amqp-client"

organization := "com.github.sstone"
 
version := "1.3-SNAPSHOT"
 
scalaVersion := "2.9.3"

crossScalaVersions := Seq("2.9.3", "2.10.4")

crossVersion := CrossVersion.binary

scalacOptions  ++= Seq("")

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")
 
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Akka 2.0.x repository" at "http://repo.akka.io/releases/"

resolvers += "SonaType" at "https://oss.sonatype.org/content/groups/public"

libraryDependencies <++= (scalaVersion) { v: String =>
  if (v.startsWith("2.10")) {
    val akkaVersion = "2.3.9"
    Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.rabbitmq" % "amqp-client" % "3.4.3",
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
      "org.scalatest"        %% "scalatest"            % "2.1.5" % "test",
      "junit" % "junit" % "4.11" % "test",
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      "ch.qos.logback" % "logback-classic" % "1.0.0"
    )
  }
  else if (v.startsWith("2.9")) {
    val akkaVersion = "2.0.5"
    Seq(
      "com.typesafe.akka" % "akka-actor" % akkaVersion withJavadoc(),
      "com.rabbitmq" % "amqp-client" % "3.4.3" withJavadoc(),
      "com.typesafe.akka" % "akka-testkit" % akkaVersion % "test",
      "org.scalatest" %% "scalatest" % "1.9.2" % "test",
      "junit" % "junit" % "4.11" % "test",
      "com.typesafe.akka" % "akka-slf4j" % akkaVersion,
      "ch.qos.logback" % "logback-classic" % "1.0.0"
    )
  }
  else Seq()
}

excludeFilter in unmanagedSources := HiddenFileFilter || "samples"