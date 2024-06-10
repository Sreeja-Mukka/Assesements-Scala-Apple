ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .settings(
    name := "spark_assement"
  )
scalaVersion := "2.12.18" // Spark 3.2.x supports Scala 2.12.x

// Define Spark version that has better compatibility with newer Java versions
val sparkVersion = "3.2.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
//  "org.apache.hadoop" % "hadoop-common" % "3.2.0",
//  "org.apache.hadoop" % "hadoop-aws" % "3.3.1" excludeAll(
//    ExclusionRule(organization = "com.google.guava", name = "guava")
//    ),
//  "com.amazonaws" % "aws-java-sdk-bundle" % "1.11.375",
  "mysql" % "mysql-connector-java" % "8.0.26"
)
dependencyOverrides += "com.google.guava" % "guava" % "27.0-jre"

import sbtassembly.AssemblyPlugin.autoImport._

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => xs match {
    case "MANIFEST.MF" :: Nil => MergeStrategy.discard // Custom strategy as an example
    case "module-info.class" :: Nil => MergeStrategy.concat
    case _ => MergeStrategy.discard // Or use other strategies as necessary
  }
  case "reference.conf" => MergeStrategy.concat
  case "application.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}