import sbt.Keys.{testFrameworks, version}
lazy val commonSettings = Seq(
  name := "N3.js",
  version := "0.1",
  scalaVersion := "2.13.5",
)

lazy val root = project.in(file(".")).
  enablePlugins(ScalaJSPlugin).
  enablePlugins(ScalaJSBundlerPlugin).
  // add the `it` configuration
  configs(IntegrationTest).
  // add `it` tasks
  settings(Defaults.itSettings: _*).
  // add Scala.js-specific settings and tasks to the `it` configuration
  settings(inConfig(IntegrationTest)(ScalaJSPlugin.testConfigSettings): _*).
  settings(
    commonSettings,
    scalaJSLinkerConfig in (Compile, fastOptJS ) ~= {
      _.withOptimizer(false)
        .withPrettyPrint(true)
        .withSourceMap(true)
    },
    scalaJSLinkerConfig in (Compile, fullOptJS) ~= {
      _.withSourceMap(false)
        .withModuleKind(ModuleKind.CommonJSModule)
    },
    webpackBundlingMode := BundlingMode.LibraryAndApplication(),
    npmDependencies in Compile ++= Seq("n3" -> "1.8.1"),
    libraryDependencies += "com.lihaoyi" %%% "utest" % "0.7.7" % "test" ,
    testFrameworks += new TestFramework("utest.runner.Framework")
  )



Global / onChangedBuildSource := ReloadOnSourceChanges
