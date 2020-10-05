name := "PrimesProxy"

version := "0.1"

scalaVersion := "2.13.3"

enablePlugins(AkkaGrpcPlugin)

lazy val akkaVersion = "2.6.9"
lazy val akkaHttpVersion = "10.2.0"
lazy val akkaGrpcVersion = "1.0.2"
lazy val scalaTestVersion = "3.2.0"
lazy val logbackVersion = "1.1.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka"     %% "akka-actor-typed"           % akkaVersion,
  "com.typesafe.akka"     %% "akka-stream"                % akkaVersion,
  "com.typesafe.akka"     %% "akka-discovery"             % akkaVersion,
  "com.typesafe.akka"     %% "akka-pki"                   % akkaVersion,
  // The Akka HTTP overwrites are required because Akka-gRPC depends on 10.1.x
  "com.typesafe.akka"     %% "akka-http"                  % akkaHttpVersion,
  "com.typesafe.akka"     %% "akka-http2-support"         % akkaHttpVersion,
  "com.typesafe.akka"     %% "akka-stream-testkit"        % akkaVersion % Test,
  "com.typesafe.akka"     %% "akka-http-testkit"          % akkaHttpVersion % Test,
  "org.scalatest"         %% "scalatest"                  % scalaTestVersion % Test,
  "ch.qos.logback"         % "logback-classic"            % logbackVersion
)
