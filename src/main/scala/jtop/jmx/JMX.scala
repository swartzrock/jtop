package jtop.jmx

import scala.scalajs.js

object JMX {
  import js.Dynamic.global

  def createClient(options: ClientConfiguration): Client = {
    val jmx = global.require("jmx")

    jmx.createClient(upickle.json.writeJs(upickle.writeJs(options)).asInstanceOf[js.Any]).asInstanceOf[Client]
  }
}
