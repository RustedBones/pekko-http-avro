addSbtPlugin("com.github.sbt" % "sbt-avro"                 % "3.5.1")
addSbtPlugin("org.typelevel"  % "sbt-typelevel"            % "0.7.7")
addSbtPlugin("org.typelevel"  % "sbt-typelevel-ci-release" % "0.7.7")

libraryDependencies += "org.apache.avro" % "avro-compiler" % "1.11.3"
