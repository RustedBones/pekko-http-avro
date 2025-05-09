// General info
val username  = "RustedBones"
val repo      = "pekko-http-avro"
val githubUrl = s"https://github.com/$username/$repo"

ThisBuild / tlBaseVersion    := "1.0"
ThisBuild / organization     := "fr.davit"
ThisBuild / organizationName := "Michel Davit"
ThisBuild / startYear        := Some(2019)
ThisBuild / licenses         := Seq(License.Apache2)
ThisBuild / homepage         := Some(url(githubUrl))
ThisBuild / scmInfo          := Some(ScmInfo(url(githubUrl), s"git@github.com:$username/$repo.git"))
ThisBuild / developers       := List(
  Developer(
    id = s"$username",
    name = "Michel Davit",
    email = "michel@davit.fr",
    url = url(s"https://github.com/$username")
  )
)

// scala versions
val scala3       = "3.3.5"
val scala213     = "2.13.16"
val defaultScala = scala3

// github actions
val java17      = JavaSpec.temurin("17")
val java11      = JavaSpec.temurin("11")
val defaultJava = java17

ThisBuild / scalaVersion                 := defaultScala
ThisBuild / crossScalaVersions           := Seq(scala3, scala213)
ThisBuild / githubWorkflowTargetBranches := Seq("main")
ThisBuild / githubWorkflowJavaVersions   := Seq(java17, java11)

// build
ThisBuild / tlFatalWarnings         := true
ThisBuild / tlJdkRelease            := Some(8)
ThisBuild / tlSonatypeUseLegacyHost := true

// mima
ThisBuild / tlBaseVersion := "1.0"
ThisBuild / mimaBinaryIssueFilters ++= Seq()

lazy val `pekko-http-avro` = (project in file("."))
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.avro,
      Dependencies.pekkoHttp,
      Dependencies.Provided.pekkoStream,
      Dependencies.Test.logback,
      Dependencies.Test.pekkoTestkit,
      Dependencies.Test.pekkoHttpTestkit,
      Dependencies.Test.scalaTest
    )
  )
