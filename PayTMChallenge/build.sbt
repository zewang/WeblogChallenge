import sbt._

val sparkVersion = "2.3.0"

lazy val root = (project in file(".")).
  settings(
    name := "PayTMChallenge",
    version := "0.1",
    scalaVersion := "2.11.12",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-hive" % sparkVersion % "provided",
      "org.apache.hadoop" % "hadoop-common" % "2.7.3" % "provided"
    ),
    resolvers ++=Seq(
      "Hortonworks Repo" at "http://repo.hortonworks.com/content/repositories/releases/",
      "Apache Repository" at "https://repository.apache.org/content/repositories/releases/",
      "MavenRepository" at "https://mvnrepository.com"
    )
  )
assemblyJarName in assembly := s"${name.value}-${version.value}.jar"
