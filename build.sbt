import scala.scalajs.sbtplugin.ScalaJSPlugin._

scalaJSSettings

name := "jtop"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.5"

resolvers += "bintray/non" at "http://dl.bintray.com/non/maven"

libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.2.5"
