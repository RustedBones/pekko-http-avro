// General info
val username = "RustedBones"
val repo     = "akka-http-avro"

lazy val commonSettings = Seq(
  organization := "fr.davit",
  organizationName := "Michel Davit",
  version := "0.1.0-SNAPSHOT",
  crossScalaVersions := Seq("2.11.12", "2.12.10", "2.13.1"), // Don't forget keep travis in sync
  scalaVersion := crossScalaVersions.value.last,
  Compile / compile / scalacOptions ++= Settings.scalacOptions(scalaVersion.value),
  homepage := Some(url(s"https://github.com/$username/$repo")),
  licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
  startYear := Some(2020),
  scmInfo := Some(ScmInfo(url(s"https://github.com/$username/$repo"), s"git@github.com:$username/$repo.git")),
  developers := List(
    Developer(
      id = s"$username",
      name = "Michel Davit",
      email = "michel@davit.fr",
      url = url(s"https://github.com/$username"))
  ),
  publishMavenStyle := true,
  Test / publishArtifact := false,
  publishTo := Some(if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging),
  credentials ++= (for {
    username <- sys.env.get("SONATYPE_USERNAME")
    password <- sys.env.get("SONATYPE_PASSWORD")
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq,
)

lazy val `akka-http-avro` = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.akkaHttp,
      Dependencies.avro,
      Dependencies.Provided.akkaStream,
      Dependencies.Provided.logback,
      Dependencies.Test.akkaTestkit,
      Dependencies.Test.akkaHttpTestkit,
      Dependencies.Test.scalaTest
    )
  )
