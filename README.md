jtop
=============

An htop-style terminal app for JMX enabled JVM apps.

Setup on OS X:

```sh
brew install node npm
npm install jmx blessed
```

## Running jtop

From terminal A:

```sh
sbt ~fastOptJS
```

From terminal B:

```sh
sbt fastOptStage::run
```

## List available MBeans from Scala REPL

```sh
scala
:load list-mbeans.scala
```