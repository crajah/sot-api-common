import sbt._

object Dependencies {
  val pureConfigVersion = "0.9.0"
  val twitterBijectionVersion = "0.9.6"
  val twitterVersion = "18.3.0"
  val finchVersion = "0.17.0"
  val sttpVersion = "1.1.9"
  val gatlingVersion = "2.2.5"
  val monocleVersion = "1.5.0"
  val circeVersion = "0.9.1"

  val scalatest = "org.scalatest" %% "scalatest" % "3.0.4"
  val mockitoScala = "org.markushauck" %% "mockitoscala" % "0.3.0"

  val typesafeConfig = "com.typesafe" % "config" % "1.3.3"
  val pureConfig = "com.github.pureconfig" %% "pureconfig" % pureConfigVersion

  val grizzledLogging = "org.clapper" %% "grizzled-slf4j" % "1.3.2"
  val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"

  val shapeless = "com.chuusai" %% "shapeless" % "2.3.3"

  val jodaTime = "joda-time" % "joda-time" % "2.9.9"

  val twitterBijectionCore = "com.twitter" %% "bijection-core" % twitterBijectionVersion
  val twitterBijectionUtil = "com.twitter" %% "bijection-util" % twitterBijectionVersion

  val twitterServer = "com.twitter" %% "twitter-server" % twitterVersion
  val twitterFinagleHttp = "com.twitter" %% "finagle-http" % twitterVersion
  val twitterFinagleNetty = "com.twitter" %% "finagle-netty3" % twitterVersion
  val twitterFinagleStats = "com.twitter" %% "finagle-stats" % twitterVersion
  val twitterUtil = "com.twitter" %% "util-collection" % twitterVersion
  val twitterServerLogback = "com.twitter" %% "twitter-server-logback-classic" % twitterVersion

  val finchCore = "com.github.finagle" %% "finch-core" % finchVersion
  val finchGeneric = "com.github.finagle" %% "finch-generic" % finchVersion
  val finchSprayJson = "com.github.finagle" %% "finch-sprayjson" % finchVersion

  val sprayJson = "io.spray" %%  "spray-json" % "1.3.4"
  val sprayJsonShapeless = "com.github.fommil" %% "spray-json-shapeless" % "1.3.0"
  val jsonLenses = "net.virtual-void" %%  "json-lenses" % "0.6.2"

  val betterFiles = "com.github.pathikrit" %% "better-files" % "3.4.0"

  val sttp = "com.softwaremill.sttp" %% "core" % sttpVersion
  val sttpOkhttpBackend = "com.softwaremill.sttp" %% "okhttp-backend" % sttpVersion
  val sttpMonixBackend = "com.softwaremill.sttp" %% "async-http-client-backend-monix" % sttpVersion
  val sttpCirce = "com.softwaremill.sttp" %% "circe" % sttpVersion

  val gatlingHighcharts = "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion
  val gatlingTestFramework = "io.gatling" % "gatling-test-framework" % gatlingVersion

  val monocleCore = "com.github.julien-truffaut" %% "monocle-core" % monocleVersion
  val monocleMacro = "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion

  val commonsLang = "org.apache.commons" % "commons-lang3" % "3.7"

  val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)
}