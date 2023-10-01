import sbt._

object Dependencies {

  object Versions {
    val avro      = "1.11.1"
    val logback   = "1.2.11"
    val pekko     = "1.0.0"
    val pekkoHttp = "1.0.0"
    val scalaTest = "3.2.12"
  }

  val pekkoHttp = "org.apache.pekko" %% "pekko-http" % Versions.pekkoHttp

  object Provided {
    val avro        = "org.apache.avro"   % "avro"            % Versions.avro    % "provided"
    val logback     = "ch.qos.logback"    % "logback-classic" % Versions.logback % "provided"
    val pekkoStream = "org.apache.pekko" %% "pekko-stream"    % Versions.pekko   % "provided"
  }

  object Test {
    val pekkoHttpTestkit = "org.apache.pekko" %% "pekko-http-testkit" % Versions.pekkoHttp % "test"
    val pekkoTestkit     = "org.apache.pekko" %% "pekko-testkit"      % Versions.pekko     % "test"
    val scalaTest        = "org.scalatest"    %% "scalatest"          % Versions.scalaTest % "test"
  }
}
