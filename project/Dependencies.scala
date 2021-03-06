import sbt._

object Dependencies {

  object Versions {
    val akka      = "2.6.13"
    val akkaHttp  = "10.2.4"
    val avro      = "1.10.1"
    val avro4s    = "3.0.8"
    val logback   = "1.2.3"
    val scalaTest = "3.2.5"
  }

  val akkaHttp = "com.typesafe.akka"  %% "akka-http"        % Versions.akkaHttp
  val avro     = "org.apache.avro"     % "avro"             % Versions.avro
  val avro4s   = "com.sksamuel.avro4s" % "avro4s-core_2.12" % Versions.avro4s

  object Provided {
    val akkaStream = "com.typesafe.akka" %% "akka-stream"     % Versions.akka    % "provided"
    val logback    = "ch.qos.logback"     % "logback-classic" % Versions.logback % "provided"
  }

  object Test {
    val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % Versions.akkaHttp  % "test"
    val akkaTestkit     = "com.typesafe.akka" %% "akka-testkit"      % Versions.akka      % "test"
    val scalaTest       = "org.scalatest"     %% "scalatest"         % Versions.scalaTest % "test"
  }
}
