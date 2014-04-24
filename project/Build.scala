import sbt._
import sbt.Keys._
import scala.Some

object ApplicationBuild extends Build {

  val appName         = "ReactiveCouchbase-es"
  val appVersion      = "0.2-SNAPSHOT"
  val appScalaVersion = "2.10.2"
  val appScalaBinaryVersion = "2.10"
  val appScalaCrossVersions = Seq("2.10.2")

  val local: Project.Initialize[Option[sbt.Resolver]] = version { (version: String) =>
    val localPublishRepo = "./repository"
    if(version.trim.endsWith("SNAPSHOT"))
      Some(Resolver.file("snapshots", new File(localPublishRepo + "/snapshots")))
    else Some(Resolver.file("releases", new File(localPublishRepo + "/releases")))
  }

  lazy val baseSettings = Defaults.defaultSettings ++ Seq(
    scalaVersion := appScalaVersion,
    scalaBinaryVersion := appScalaBinaryVersion,
    crossScalaVersions := appScalaCrossVersions,
    parallelExecution in Test := false
  )

  lazy val root = Project("root", base = file("."))
    .settings(baseSettings: _*)
    .settings(
      publishLocal := {},
      publish := {}
    ).aggregate(
      eventstore
    )

  lazy val eventstore = Project(appName, base = file("eventstore"))
    .settings(baseSettings: _*)
    .settings(
      resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
      resolvers += "Spy Repository" at "http://files.couchbase.com/maven2",
      resolvers += "krasserm at bintray" at "http://dl.bintray.com/krasserm/maven",
      libraryDependencies += "org.reactivecouchbase" %% "reactivecouchbase-core" % "0.2-SNAPSHOT",
      libraryDependencies += "com.github.krasserm" %% "akka-persistence-testkit" % "0.3" % "test",
      libraryDependencies += "com.typesafe.akka" %% "akka-persistence-experimental" % "2.3.0",
      organization := "org.reactivecouchbase",
      version := appVersion,
      publishTo <<= local,
      publishMavenStyle := true,
      publishArtifact in Test := false,
      pomIncludeRepository := { _ => false }
    )
}
