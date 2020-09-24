ThisBuild / resolvers ++= Seq(
    "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
    Resolver.mavenLocal
)

name := "ghtestflinksbt"

version := "0.1-SNAPSHOT"

organization := "ch.iwerk"

ThisBuild / scalaVersion := "2.12.12"

val flinkVersion = "1.11.2"

val flinkDependencies = Seq(
   "org.apache.flink" %% "flink-clients" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided",


  //"org.apache.flink" %% "flink-clients" % flinkVersion,
  //"org.apache.flink" %% "flink-scala" % flinkVersion,
  //"org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
  "org.scala-lang.modules" %% "scala-xml" % "1.3.0",
  "org.marc4j" % "marc4j" % "2.9.1",
  "net.sf.saxon" % "Saxon-HE" % "10.1"
)


lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= flinkDependencies

  )

assembly / mainClass := Some("ch.iwerk.Job")

// make run command include the provided dependencies
Compile / run  := Defaults.runTask(Compile / fullClasspath,
                                   Compile / run / mainClass,
                                   Compile / run / runner
                                  ).evaluated

// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
Compile / run / fork := true
Global / cancelable := true

// exclude Scala library from assembly
assembly / assemblyOption  := (assembly / assemblyOption).value.copy(includeScala = false)
