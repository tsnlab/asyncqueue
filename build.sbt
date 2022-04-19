def scalacOptionsVersion(scalaVersion: String): Seq[String] = {
  Seq() ++ {
    // If we're building with Scala > 2.11, enable the compile option
    //  switch to support our anonymous Bundle definitions:
    //  https://github.com/scala/bug/issues/10047
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, scalaMajor: Long)) if scalaMajor < 12 => Seq()
      case _ => Seq("-Xsource:2.13")
    }
  }
}

def javacOptionsVersion(scalaVersion: String): Seq[String] = {
  Seq() ++ {
    // Scala 2.12 requires Java 8. We continue to generate
    //  Java 7 compatible code for Scala 2.11
    //  for compatibility with old clients.
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, scalaMajor: Long)) if scalaMajor < 12 =>
        Seq("-source", "1.7", "-target", "1.7")
      case _ =>
        Seq("-source", "1.8", "-target", "1.8")
    }
  }
}

name := "asyncqueue-lite"

version := "1.0.0"

scalaVersion := "2.13.6"

crossScalaVersions := Seq("2.11.12", "2.12.4", "2.13.6")

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "edu.berkeley.cs" %% "chisel3" % "3.5.+",
    "edu.berkeley.cs" %% "chiseltest" % "0.5.+" % "test"
  ),
  scalacOptions ++= Seq(
    "-Ymacro-annotations",
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature",
    "-Xcheckinit"
  ),
  addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % "3.5.+" cross CrossVersion.full),
)

lazy val root = (project in file("."))
  .settings(commonSettings, Seq(
    name := "com.tsnlab.ipcore.npu",
    libraryDependencies ++= Seq(
      "org.easysoc" %% "layered-firrtl" % "1.1-SNAPSHOT",
    ),
  ))