import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"  % "7.11.0"
  )

  val test = Seq(
    "org.scalatest"           %% "scalatest"                  % "3.2.14"              % "test,it",
    "org.scalatestplus.play"  %% "scalatestplus-play"         % "5.1.0"               % "test",
    "com.vladsch.flexmark"    %  "flexmark-all"               % "0.62.2"              % "test, it"
  )
}
