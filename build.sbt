// General info
val username = "RustedBones"
val repo     = "akka-http-avro"

// for sbt-github-actions
ThisBuild / crossScalaVersions := Seq("2.13.8", "2.12.15")
ThisBuild / githubWorkflowBuild := Seq(
  WorkflowStep.Sbt(name = Some("Check project"), commands = List("scalafmtCheckAll", "headerCheckAll")),
  WorkflowStep.Sbt(name = Some("Build project"), commands = List("test"))
)
ThisBuild / githubWorkflowTargetBranches := Seq("main")
ThisBuild / githubWorkflowPublishTargetBranches := Seq.empty

lazy val commonSettings = Seq(
  organization := "fr.davit",
  organizationName := "Michel Davit",
  crossScalaVersions := (ThisBuild / crossScalaVersions).value,
  scalaVersion := crossScalaVersions.value.head,
  homepage := Some(url(s"https://github.com/$username/$repo")),
  licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
  startYear := Some(2020),
  scmInfo := Some(ScmInfo(url(s"https://github.com/$username/$repo"), s"git@github.com:$username/$repo.git")),
  developers := List(
    Developer(
      id = s"$username",
      name = "Michel Davit",
      email = "michel@davit.fr",
      url = url(s"https://github.com/$username")
    )
  ),
  publishMavenStyle := true,
  Test / publishArtifact := false,
  publishTo := Some(if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging),
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  credentials ++= (for {
    username <- sys.env.get("SONATYPE_USERNAME")
    password <- sys.env.get("SONATYPE_PASSWORD")
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
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
