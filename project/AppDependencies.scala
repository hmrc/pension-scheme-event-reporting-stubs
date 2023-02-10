import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"  % "7.13.0"
  )

  val test = Seq(
    "org.scalatest"           %% "scalatest"                  % "3.2.15"              % "test,it",
    "org.scalatestplus.play"  %% "scalatestplus-play"         % "5.1.0"               % "test",
    "com.vladsch.flexmark"    %  "flexmark-all"               % "0.64.0"              % "test, it"
  )
}
