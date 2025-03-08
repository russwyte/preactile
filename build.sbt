import org.scalajs.linker.interface.ModuleSplitStyle

val scala3Version = "3.6.4"
val zioVersion    = "2.1.16"

ThisBuild / scalaVersion := scala3Version

ThisBuild / libraryDependencies ++= Seq(
  "dev.zio" %%% "zio"               % zioVersion,
  "dev.zio" %%% "zio-test"          % zioVersion % Test,
  "dev.zio" %%% "zio-test-sbt"      % zioVersion % Test,
  "dev.zio" %%% "zio-test-magnolia" % zioVersion % Test,
)

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-Wunused:imports",
  "-language:implicitConversions",
)

lazy val root = project
  .in(file("."))
  .aggregate(core, preactileConduit, example)
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
  )

lazy val preactileConduit = project
  .in(file("conduit"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(core)
  .settings(
    name := "preactile-conduit",
    libraryDependencies ++= Seq(
      "io.github.russwyte" %%% "conduit"              % "0.0.4",
      "io.github.cquiroz"  %%% "scala-java-time"      % "2.6.0",
      "io.github.cquiroz"  %%% "scala-java-time-tzdb" % "2.6.0",
    ),
  )

lazy val example = project
  .in(file("example"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(core, preactileConduit)
  .settings(
    name                            := "preactile-example",
    publish / skip                  := true,
    test / skip                     := true,
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("todo")))
    },
    libraryDependencies += ("org.scala-js" %%% "scalajs-java-securerandom" % "1.0.0").cross(CrossVersion.for3Use2_13),
  )
