package htop

object Main extends scala.scalajs.js.JSApp {
  import scala.scalajs.js
  import js.Dynamic
  import js.Dynamic.global
  import Implicits._

  def main(): Unit = {
    val jmx = global.require("jmx")

    println("Creating client")

    val client = jmx.createClient(Client(host = "localhost", port = 8855))

    println("Connecting...")
    client.connect()
    client.on("connect", () => {
      println("Requesting heap memory usage...")

      client.getAttribute("java.lang:type=Memory", "HeapMemoryUsage", (data: Dynamic) => {
        println("Data returned.")
        val used = data.getSync("used")
        println(s"HeapMemoryUsage used: $used")
      })

      client.setAttribute("java.lang:type=Memory", "Verbose", true, () => {
        println("Memory verbose on") // callback is optional
      })

      client.invoke("java.lang:type=Memory", "gc", js.Array(), (data: Dynamic) => {
        println("gc() done")
      })
    })

  }
}
