import sbt._

object Dependencies {

  object Versions {
    val avro      = "1.11.3"
    val logback   = "1.5.8"
    val pekko     = "1.0.2"
    val pekkoHttp = "1.0.1"
    val scalaTest = "3.2.18"
  }

  val avro      = "org.apache.avro"   % "avro"       % Versions.avro
  val pekkoHttp = "org.apache.pekko" %% "pekko-http" % Versions.pekkoHttp

  object Provided {
    val pekkoStream = "org.apache.pekko" %% "pekko-stream" % Versions.pekko % "provided"
  }

  object Test {
    val logback          = "ch.qos.logback"    % "logback-classic"    % Versions.logback   % "test"
    val pekkoHttpTestkit = "org.apache.pekko" %% "pekko-http-testkit" % Versions.pekkoHttp % "test"
    val pekkoTestkit     = "org.apache.pekko" %% "pekko-testkit"      % Versions.pekko     % "test"
    val scalaTest        = "org.scalatest"    %% "scalatest"          % Versions.scalaTest % "test"
  }
}
