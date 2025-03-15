import org.scalajs.linker.interface.ModuleSplitStyle
import xerial.sbt.Sonatype.sonatypeCentralHost
usePgpKeyHex("2F64727A87F1BCF42FD307DD8582C4F16659A7D6")

val scala3Version = "3.6.4"
val zioVersion    = "2.1.16"

ThisBuild / scalaVersion := scala3Version

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-Wunused:imports",
  "-language:implicitConversions",
)

ThisBuild / publishMavenStyle.withRank(KeyRanks.Invisible)    := true
ThisBuild / pomIncludeRepository.withRank(KeyRanks.Invisible) := { _ => false }
ThisBuild / sonatypeCredentialHost                            := sonatypeCentralHost
ThisBuild / publishTo                                         := sonatypePublishToBundle.value
ThisBuild / licenses             := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage             := Some(url("https://github.com/russwyte/preactile"))
ThisBuild / organization         := "io.github.russwyte"
ThisBuild / organizationName     := "russwyte"
ThisBuild / organizationHomepage := Some(url("https://github.com/russwyte"))
ThisBuild / versionScheme        := Some("early-semver")
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/russwyte/preactile"),
    "scm:git@github.com:russwyte/preactile.git",
  )
)
ThisBuild / developers := List(
  Developer(
    id = "russwyte",
    name = "Russ White",
    email = "356303+russwyte@users.noreply.github.com",
    url = url("https://github.com/russwyte"),
  )
)

lazy val root = project
  .in(file("."))
  .aggregate(core, preactileConduit, example)
  .settings(
    name           := "preactile-root",
    publish / skip := true,
  )

lazy val core = project
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    name                                  := "preactile",
    Compile / npmDependencies += "preact" -> "^10.26.4",
    installJsdom / version                := "^26.0.0",
    Test / requireJsDomEnv                := true,
    webpackBundlingMode                   := BundlingMode.LibraryOnly(),
    scalaJSUseMainModuleInitializer       := true,
    webpack / version                     := "5.98.0",
    libraryDependencies ++= Seq(
      "dev.zio"      %%% "zio"          % zioVersion % Test,
      "dev.zio"      %%% "zio-test"     % zioVersion % Test,
      "dev.zio"      %%% "zio-test-sbt" % zioVersion % Test,
      "org.scala-js" %%% "scalajs-dom"  % "2.8.0",
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
