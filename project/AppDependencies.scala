import sbt.*

object AppDependencies {
  private val bootstrapVersion = "10.8.0"
  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"                   %% "bootstrap-backend-play-30"  % bootstrapVersion,
    "com.networknt"                 %  "json-schema-validator"      % "1.5.8",
    "com.fasterxml.jackson.module"  %% "jackson-module-scala"       % "2.21.2",
    "org.scala-lang.modules"        %% "scala-java8-compat"         % "1.0.2"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-30" % bootstrapVersion % Test
  )
}
