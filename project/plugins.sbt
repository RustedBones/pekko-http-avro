addSbtPlugin("com.github.sbt" % "sbt-avro"                 % "3.4.4")
addSbtPlugin("org.typelevel"  % "sbt-typelevel"            % "0.7.6")
addSbtPlugin("org.typelevel"  % "sbt-typelevel-ci-release" % "0.7.6")

libraryDependencies += "org.apache.avro" % "avro-compiler" % "1.11.3"
