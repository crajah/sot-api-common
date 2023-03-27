import scala.language.postfixOps
import sbt.Resolver
import sbt.Keys.{libraryDependencies, publishTo}
import com.amazonaws.regions.{Region, Regions}
import com.scalapenos.sbt.prompt.SbtPrompt.autoImport._
import Dependencies._
import SotDependencies._

lazy val scala_2_12 = "2.12.4"
lazy val sbt_1_1_1 = "1.1.1"

lazy val IT = config("it") extend Test

lazy val root = (project in file(".")).enablePlugins(GatlingPlugin)
  .configs(IT, IntegrationTest, GatlingIt)
  .settings(Defaults.itSettings: _*)
  .settings(inConfig(IT)(Defaults.testSettings): _*)
  .settings(Revolver.settings)
  .settings(
    name := "sot-api-common",
    inThisBuild(
      List(
        organization := "parallelai",
        scalaVersion := scala_2_12
      )
    ),
    promptTheme := com.scalapenos.sbt.prompt.PromptThemes.ScalapenosTheme,
    scalacOptions ++= Seq(
      "-deprecation",           // Emit warning and location for usages of deprecated APIs.
      "-feature",               // Emit warning and location for usages of features that should be imported explicitly.
      "-unchecked",             // Enable additional warnings where generated code depends on assumptions.
      "-Xlint",                 // Enable recommended additional warnings.
      "-Ywarn-adapted-args",    // Warn if an argument list is modified to match the receiver.
      "-Ywarn-dead-code",
      "-Ywarn-value-discard",   // Warn when non-Unit expression results are unused.
      "-Ypartial-unification",
      "-language:postfixOps",
      "-language:higherKinds",
      "-language:existentials"/*,
      "-Xlog-implicits"*/
    ),
    s3region := Region.getRegion(Regions.EU_WEST_2),
    publishArtifact in (Test, packageBin) := true,
    publishArtifact in (IntegrationTest, packageBin) := true,
    publishTo := {
      val prefix = if (isSnapshot.value) "snapshot" else "release"
      Some(s3resolver.value(s"Parallel AI $prefix S3 bucket", s3(s"$prefix.repo.parallelai.com")) withMavenPatterns)
    },
    resolvers ++= Seq[Resolver](
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots"),
      s3resolver.value("Parallel AI S3 Releases resolver", s3("release.repo.parallelai.com")) withMavenPatterns,
      s3resolver.value("Parallel AI S3 Snapshots resolver", s3("snapshot.repo.parallelai.com")) withMavenPatterns
    ),
    resolvers += sbtResolver.value,
    libraryDependencies ++= Seq(
      scalatest % "test, it",
      mockitoScala % "test, it",
      gatlingHighcharts % "it",
      gatlingTestFramework % "it"
    ),
    excludeDependencies ++= Seq(
      "org.scala-lang.modules" % "scala-xml_2.11",
      "org.scala-lang.modules" % "scala-parser-combinators_2.11"
    ),
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
      "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.0",
      shapeless,
      typesafeConfig,
      pureConfig,
      grizzledLogging,
      logback,
      jodaTime,
      twitterBijectionCore,
      twitterBijectionUtil,
      twitterServer,
      twitterFinagleHttp,
      twitterFinagleNetty,
      twitterFinagleStats,
      twitterUtil,
      twitterServerLogback,
      finchCore,
      finchGeneric,
      finchSprayJson,
      jsonLenses,
      sprayJsonShapeless,
      betterFiles,
      sttp,
      sttpOkhttpBackend,
      sttpMonixBackend,
      sttpCirce,
      monocleCore,
      monocleMacro,
      commonsLang
    ) ++ circe,
    libraryDependencies ++= Seq(
      sotCommonSecure
    )
  )

fork in run := true

fork in Test := true

fork in IT := true