addSbtPlugin("com.github.sbt" % "sbt-avro"                 % "3.4.0")
addSbtPlugin("org.typelevel"  % "sbt-typelevel"            % "0.6.2")
addSbtPlugin("org.typelevel"  % "sbt-typelevel-ci-release" % "0.6.2")

libraryDependencies += "org.apache.avro" % "avro-compiler" % "1.11.3"
