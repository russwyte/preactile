val scala3Version = "3.6.3"
val zioVersion    = "2.1.16"

ThisBuild / scalaVersion := scala3Version

ThisBuild / libraryDependencies ++= Seq(
  "dev.zio" %%% "zio"               % zioVersion,
  "dev.zio" %%% "zio-test"          % zioVersion % Test,
  "dev.zio" %%% "zio-test-sbt"      % zioVersion % Test,
  "dev.zio" %%% "zio-test-magnolia" % zioVersion % Test,
)

lazy val root = project
  .in(file("."))
  .aggregate(core)
  .settings(
    name           := "preactile",
    publish / skip := true,
  )

lazy val core = project
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "preactile-core",
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.8.0"
    ),
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-Wunused:all",
      "-language:implicitConversions",
    ),
  )
