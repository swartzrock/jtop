package jtop

import jtop.jmx.{JMX, Client, ClientConfiguration}

object Main extends scala.scalajs.js.JSApp {
  import scala.scalajs.js
  import js.Dynamic
  import js.Dynamic.global

  def main(): Unit = {
    // println("Creating client")
    val client = JMX.createClient(ClientConfiguration(host = "localhost", port = 8855))

    // println("Connecting...")
    client.connect()
    client.on("connect", () => {
      refresh(client)
      renderScreen()
    })


  }

  def refresh(client: Client): Unit = {
    client.getAttribute("java.lang:type=Memory", "HeapMemoryUsage", (data: Dynamic) => {
      val used = data.getSync("used")
      // println(s"HeapMemoryUsage used: $used")
    })

    client.getAttribute("java.lang:type=Threading", "ThreadCount", (count: Dynamic) => {
      // println(s"ThreadCount: $count")
    })

    client.getAttribute("java.lang:type=ClassLoading", "TotalLoadedClassCount", (count: Dynamic) => {
      // println(s"TotalLoadedClassCount: $count")
    })

    // HEAP

    client.getAttribute("java.lang:type=MemoryPool,name=PS Eden Space", "Usage", (data: Dynamic) => {
      val used = data.getSync("used")
      // println(s"PS Eden Space used: $used")
    })

    client.getAttribute("java.lang:type=MemoryPool,name=PS Survivor Space", "Usage", (data: Dynamic) => {
      val used = data.getSync("used")
      // println(s"PS Survivor Space used: $used")
    })

    client.getAttribute("java.lang:type=MemoryPool,name=PS Old Gen", "Usage", (data: Dynamic) => {
      val used = data.getSync("used")
      // println(s"PS Old Gen used: $used")
    })

    // NON_HEAP

    client.getAttribute("java.lang:type=MemoryPool,name=Code Cache", "Usage", (data: Dynamic) => {
      val used = data.getSync("used")
      // println(s"Code Cache used: $used")
    })

    client.getAttribute("java.lang:type=MemoryPool,name=Compressed Class Space", "Usage", (data: Dynamic) => {
      val used = data.getSync("used")
      // println(s"Compressed Class Space used: $used")
    })

    client.getAttribute("java.lang:type=MemoryPool,name=Metaspace", "Usage", (data: Dynamic) => {
      val used = data.getSync("used")
      // println(s"Metaspace used: $used")
    })
  }




  var heapUsagePercentData: Array[Double] = Array(40.0, 45.0, 51.0, 62.0, 90.0, 63.0)
  var loadedClassesData: Array[Double] = Array(200.0, 900.0, 2403.0, 9912.0, 12100.0)
  var threadsData: Array[Double] = Array(20.0, 21.0, 28.0, 28.0, 25.0, 32.0, 31.0, 35.0)

  var heapUsageBarsData: Array[Double] = Array(20.0, 15.0, 40.0)
  var offheapUsageBarsData: Array[Double] = Array(34.0, 22.0, 14.0)


  def renderScreen(): Unit = {

    val blessed = global.require("blessed")
    val contrib = global.require("blessed-contrib")

    // println(s"Creating widgets")

    val screen = blessed.screen()

    val mainGrid = js.Dynamic.newInstance(contrib.grid)(js.Dynamic.literal(rows = 1, cols = 2))

    val gridLeft = js.Dynamic.newInstance(contrib.grid)(js.Dynamic.literal(rows = 2, cols = 1))

    gridLeft.set(0, 0, contrib.line,
      js.Dynamic.literal(maxY = 100, showNthLabel = 9999, label = "Heap Memory Usage",
      style = js.Dynamic.literal(line = "blue", text = "white"))
    )
    gridLeft.set(1, 0, contrib.line,
      js.Dynamic.literal(showNthLabel = 9999, label = "Loaded Classes",
      style = js.Dynamic.literal(line = "green", text = "white"))
    )


    val gridBottomRight = js.Dynamic.newInstance(contrib.grid)(js.Dynamic.literal(rows = 1, cols = 2))
    gridBottomRight.set(0, 0, contrib.bar,
      js.Dynamic.literal(barWidth = 5, barSpacing = 10, maxHeight = 10, label = "Heap Usage (%)")
    )
    gridBottomRight.set(0, 1, contrib.bar,
      js.Dynamic.literal(barWidth = 5, barSpacing = 10, maxHeight = 10, label = "Off-Heap Usage (%)")
    )



    val gridRight = js.Dynamic.newInstance(contrib.grid)(js.Dynamic.literal(rows = 2, cols = 1))
    gridRight.set(0, 0, contrib.line,
      js.Dynamic.literal(maxY = 100, showNthLabel = 9999, label = "Threads",
      style = js.Dynamic.literal(line = "red", text = "white"))
    )
    gridRight.set(1, 0, gridBottomRight)



    mainGrid.set(0, 0, gridLeft)
    mainGrid.set(0, 1, gridRight)

    mainGrid.applyLayout(screen)

    val heapUsageLine = gridLeft.get(0, 0)
    val loadedClassesLine = gridLeft.get(1, 0)

    val threadsLine = gridRight.get(0, 0)
    val heapUsageBars = gridBottomRight.get(0, 0)
    val offHeapUsageBars = gridBottomRight.get(0, 1)


    heapUsageLine.setData(Array[String](" "), heapUsagePercentData)
    loadedClassesLine.setData(Array[String](" "), loadedClassesData)
    threadsLine.setData(Array[String](" "), threadsData)

    heapUsageBars.setData(js.Dynamic.literal(titles = Array[String]("OldGen", "Eden", "Survivor"), data = heapUsageBarsData))
    offHeapUsageBars.setData(js.Dynamic.literal(titles = Array[String]("Meta", "Cache", "Compr"), data = offheapUsageBarsData))

    screen.render()

  }

}

