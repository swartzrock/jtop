package htop

import scala.scalajs.js

object Main extends scala.scalajs.js.JSApp {
  def main(): Unit = {
    import js.Dynamic.global
    val foo = global.require("jmx")
    println("Hello World!")
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
