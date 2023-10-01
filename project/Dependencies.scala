import sbt._

object Dependencies {

  object Versions {
    val pekko     = "1.0.0"
    val pekkoHttp = "1.0.0"
    val avro      = "1.11.0"
    val avro4s    = "3.0.8"
    val logback   = "1.2.11"
    val scalaTest = "3.2.12"
  }

  val pekkoHttp = "org.apache.pekko"   %% "pekko-http"       % Versions.pekkoHttp
  val avro      = "org.apache.avro"     % "avro"             % Versions.avro
  val avro4s    = "com.sksamuel.avro4s" % "avro4s-core_2.12" % Versions.avro4s

  object Provided {
    val pekkoStream = "org.apache.pekko" %% "pekko-stream"    % Versions.pekko   % "provided"
    val logback     = "ch.qos.logback"    % "logback-classic" % Versions.logback % "provided"
  }

  object Test {
    val pekkoHttpTestkit = "org.apache.pekko" %% "pekko-http-testkit" % Versions.pekkoHttp % "test"
    val pekkoTestkit     = "org.apache.pekko" %% "pekko-testkit"      % Versions.pekko     % "test"
    val scalaTest        = "org.scalatest"    %% "scalatest"          % Versions.scalaTest % "test"
  }
}
