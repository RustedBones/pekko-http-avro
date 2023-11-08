addSbtPlugin("com.github.sbt" % "sbt-avro"                 % "3.4.0")
addSbtPlugin("org.typelevel"  % "sbt-typelevel"            % "0.5.4")
addSbtPlugin("org.typelevel"  % "sbt-typelevel-ci-release" % "0.5.4")

libraryDependencies += "org.apache.avro" % "avro-compiler" % "1.11.3"
