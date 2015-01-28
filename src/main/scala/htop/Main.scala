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

    println("Requesting heap memory usage...")
    client.getAttribute("java.lang:type=Memory", "HeapMemoryUsage", (data: Dynamic) => {
      println("Data returned.")
      val used = data.getSync("used")
      println(s"HeapMemoryUsage used: $used")
    })

    /*
client = jmx.createClient({
  host: "localhost", // optional
  port: 3000
});

client.connect();
client.on("connect", function() {

  client.getAttribute("java.lang:type=Memory", "HeapMemoryUsage", function(data) {
    var used = data.getSync('used');
    console.log("HeapMemoryUsage used: " + used.longValue);
    // console.log(data.toString());
  });

  client.setAttribute("java.lang:type=Memory", "Verbose", true, function() {
    console.log("Memory verbose on"); // callback is optional
  });

  client.invoke("java.lang:type=Memory", "gc", [], function(data) {
    console.log("gc() done");
  });

});
client = jmx.createClient({
  service: "service:jmx:rmi:///jndi/rmi://localhost:3000/jmxrmi"
});
    * */
  }
}
