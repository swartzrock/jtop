package jtop

import jtop.jmx.{JMX, Client, ClientConfiguration}

object Main extends scala.scalajs.js.JSApp {
  import scala.scalajs.js
  import js.Dynamic
  import js.Dynamic.global

  var screen: js.Dynamic = null
  var heapUsageLine: js.Dynamic = null
  var loadedClassesLine: js.Dynamic = null
  var threadsLine: js.Dynamic = null
  var heapUsageBars: js.Dynamic = null
  var offHeapUsageBars: js.Dynamic = null

  var heapUsagePercentData = Array[Double](0.0)
  var loadedClassesData = Array[Double](0.0)
  var threadsData = Array[Double](0.0)
  val heapUsageBarsData = Array[Double](0.0, 0.0, 0.0)
  val offheapUsageBarsData = Array[Double](0.0, 0.0, 0.0)

  def main(): Unit = {
    initScreen()

    val client = JMX.createClient(ClientConfiguration(host = "localhost", port = 8855))
    client.connect()
    client.on("connect", () => {
      refreshData(client)
      renderScreen()

      global.setInterval(() => {
        refreshData(client)
        renderScreen()
      }, 300)
    })
  }

  def initScreen() = {
    val blessed = global.require("blessed")
    val contrib = global.require("blessed-contrib")

    screen = blessed.screen()

    val mainGrid = js.Dynamic.newInstance(contrib.grid)(js.Dynamic.literal(rows = 1, cols = 2))

    val gridLeft = js.Dynamic.newInstance(contrib.grid)(js.Dynamic.literal(rows = 2, cols = 1))

    gridLeft.set(0, 0, contrib.line,
      js.Dynamic.literal(maxY = 100, showNthLabel = 9999, label = "Heap Memory Usage")
    )
    gridLeft.set(1, 0, contrib.line,
      js.Dynamic.literal(maxY = 10000, showNthLabel = 9999, label = "Loaded Classes")
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
      js.Dynamic.literal(maxY = 100, showNthLabel = 9999, label = "Threads")
    )
    gridRight.set(1, 0, gridBottomRight)

    mainGrid.set(0, 0, gridLeft)
    mainGrid.set(0, 1, gridRight)

    mainGrid.applyLayout(screen)

    heapUsageLine = gridLeft.get(0, 0)
    loadedClassesLine = gridLeft.get(1, 0)

    threadsLine = gridRight.get(0, 0)
    heapUsageBars = gridBottomRight.get(0, 0)
    offHeapUsageBars = gridBottomRight.get(0, 1)
  }

  def refreshData(client: Client): Unit = {
    client.getAttribute("java.lang:type=Memory", "HeapMemoryUsage", (data: Dynamic) => {
      val used = data.getSync("used").toString.toDouble
      heapUsagePercentData = heapUsagePercentData :+ used
    })

    client.getAttribute("java.lang:type=Threading", "ThreadCount", (count: Dynamic) => {
      threadsData = threadsData :+ count.toString.toDouble
    })

    client.getAttribute("java.lang:type=ClassLoading", "TotalLoadedClassCount", (count: Dynamic) => {
      loadedClassesData = loadedClassesData :+ count.toString.toDouble
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

  def renderScreen(): Unit = {
    heapUsageLine.setData(Array[String](" "), heapUsagePercentData)
    loadedClassesLine.setData(Array[String](" "), loadedClassesData)
    threadsLine.setData(Array[String](" "), threadsData)

    heapUsageBars.setData(js.Dynamic.literal(titles = Array[String]("OldGen", "Eden", "Survivor"), data = heapUsageBarsData))
    offHeapUsageBars.setData(js.Dynamic.literal(titles = Array[String]("OldGen", "Eden", "Survivor"), data = offheapUsageBarsData))

    screen.render()
  }

}

