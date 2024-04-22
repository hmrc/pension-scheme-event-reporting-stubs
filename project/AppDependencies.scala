import sbt._

object AppDependencies {
  private val bootstrapVersion = "8.4.0"
  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-30"  % bootstrapVersion,
    "com.github.pjfanning"    %% "scala-faker" % "0.5.3"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"  % bootstrapVersion      % Test,
    "org.scalatest"           %% "scalatest"                  % "3.2.15"              % Test,
    "org.scalatestplus.play"  %% "scalatestplus-play"         % "5.1.0"               % Test,
    "com.vladsch.flexmark"    %  "flexmark-all"               % "0.64.0"              % Test
  )
}
