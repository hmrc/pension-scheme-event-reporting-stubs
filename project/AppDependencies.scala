import sbt._

object AppDependencies {
  private val playVersion = "7.13.0"
  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"  % playVersion,
    "com.github.pjfanning" %% "scala-faker" % "0.5.3"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"  % playVersion           % "test,it",
    "org.scalatest"           %% "scalatest"                  % "3.2.15"              % "test,it",
    "org.scalatestplus.play"  %% "scalatestplus-play"         % "5.1.0"               % "test",
    "com.vladsch.flexmark"    %  "flexmark-all"               % "0.64.0"              % "test, it"
  )
}
