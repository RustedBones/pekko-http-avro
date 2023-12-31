import sbt._

object Dependencies {

  object Versions {
    val avro      = "1.11.3"
    val logback   = "1.4.14"
    val pekko     = "1.0.2"
    val pekkoHttp = "1.0.0"
    val scalaTest = "3.2.17"
  }

  val avro      = "org.apache.avro"   % "avro"       % Versions.avro
  val pekkoHttp = "org.apache.pekko" %% "pekko-http" % Versions.pekkoHttp

  object Provided {
    val logback     = "ch.qos.logback"    % "logback-classic" % Versions.logback % "provided"
    val pekkoStream = "org.apache.pekko" %% "pekko-stream"    % Versions.pekko   % "provided"
  }

  object Test {
    val pekkoHttpTestkit = "org.apache.pekko" %% "pekko-http-testkit" % Versions.pekkoHttp % "test"
    val pekkoTestkit     = "org.apache.pekko" %% "pekko-testkit"      % Versions.pekko     % "test"
    val scalaTest        = "org.scalatest"    %% "scalatest"          % Versions.scalaTest % "test"
  }
}
