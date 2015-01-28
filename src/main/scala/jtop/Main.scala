package jtop

import jtop.jmx.{JMX, ClientConfiguration}

object Main extends scala.scalajs.js.JSApp {
  import scala.scalajs.js
  import js.Dynamic
  import js.Dynamic.global

  def main(): Unit = {
    println("Creating client")
    val client = JMX.createClient(ClientConfiguration(host = "localhost", port = 8855))

    println("Connecting...")
    client.connect()
    client.on("connect", () => {
      println("Requesting heap memory usage...")

      client.getAttribute("java.lang:type=Memory", "HeapMemoryUsage", (data: Dynamic) => {
        println("Data returned.")
        val used = data.getSync("used")
        println(s"HeapMemoryUsage used: $used")
      })

    })

  }
}
