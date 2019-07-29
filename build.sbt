name := """jira_sample"""
organization := "com.sapient"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

resolvers +=
  "atlassian-public" at "https://maven.atlassian.com/content/repositories/atlassian-public/"

libraryDependencies += guice
libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test,
  "com.amazonaws" % "aws-java-sdk" % "1.11.498",
  "log4j" % "log4j" % "1.2.17",
  "org.apache.commons" % "commons-io" % "1.3.2",
  "com.typesafe" % "config" % "1.3.2",
  "com.google.cloud" % "google-cloud-vision" % "1.64.0",
  "com.sun.mail"%"javax.mail"%"1.5.5",
    "net.rcarz" % "jira-client" % "0.5"
  
 
)
