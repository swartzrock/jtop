import scala.scalajs.sbtplugin.ScalaJSPlugin._

scalaJSSettings

name := "jtop"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.5"

resolvers += "bintray/non" at "http://dl.bintray.com/non/maven"

libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.2.5"

ScalaJSKeys.persistLauncher := true

mainClass := Some("jtop.Main")

ScalaJSKeys.requiresDOM := false

//val copyAndMunge = taskKey[Unit]("")
//
//copyAndMunge := {
//  import java.io._
//  println("HERE")
//  IO.copyFile(new File("target/scala-2.11/jtop-fastopt.js"), new File("jtop.js"))
//}
//
//copyAndMunge := copyAndMunge.triggeredBy(ScalaJSKeys.fastOptJS)

