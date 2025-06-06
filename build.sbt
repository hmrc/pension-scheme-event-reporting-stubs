import uk.gov.hmrc.DefaultBuildSettings

val appName = "pension-scheme-event-reporting-stubs"

ThisBuild / majorVersion := 0
ThisBuild / scalaVersion := "3.7.0"
ThisBuild / scalacOptions ++= Seq(
  "-feature",
  "-Wconf:src=routes/.*:s",
  "-Xfatal-warnings",
  "-Wconf:msg=Flag.*repeatedly:s",
  "-deprecation"
)

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(
    PlayKeys.playDefaultPort := 8217,
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
  )
  .settings(CodeCoverageSettings.settings *)
  .settings(
    Test / parallelExecution := true,
    Test / fork := true,
    Test / javaOptions += "-Dconfig.file=conf/test.application.conf"
  )

lazy val it = project
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test") // the "test->test" allows reusing test code and test dependencies
  .settings(DefaultBuildSettings.itSettings())
  .settings(libraryDependencies ++= AppDependencies.test)