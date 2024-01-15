addSbtPlugin("com.github.sbt" % "sbt-avro"                 % "3.4.4")
addSbtPlugin("org.typelevel"  % "sbt-typelevel"            % "0.6.5")
addSbtPlugin("org.typelevel"  % "sbt-typelevel-ci-release" % "0.6.5")

libraryDependencies += "org.apache.avro" % "avro-compiler" % "1.11.3"
