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
      js.Dynamic.literal(showNthLabel = 9999, label = "Heap Memory Usage (Mb)",
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
      js.Dynamic.literal(showNthLabel = 9999, label = "Threads",
        style = js.Dynamic.literal(line = "red", text = "white"))
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
      val used = data.getSync("used").toString.toDouble / 1048576.0
      heapUsagePercentData = heapUsagePercentData :+ used
    })

    client.getAttribute("java.lang:type=Threading", "ThreadCount", (count: Dynamic) => {
      threadsData = threadsData :+ count.toString.toDouble
    })

    client.getAttribute("java.lang:type=ClassLoading", "TotalLoadedClassCount", (count: Dynamic) => {
      loadedClassesData = loadedClassesData :+ count.toString.toDouble
    })

    def percentFor(data: Dynamic): Double = {
      val used = data.getSync("used").toString.toDouble
      val max = data.getSync("max").toString.toDouble
      math.abs(math.ceil((used / max) * 100.0D))
    }
    // HEAP

    client.getAttribute("java.lang:type=MemoryPool,name=PS Eden Space", "Usage", (data: Dynamic) => {
      heapUsageBarsData(1) = percentFor(data)
    })

    client.getAttribute("java.lang:type=MemoryPool,name=PS Survivor Space", "Usage", (data: Dynamic) => {
      heapUsageBarsData(2) = percentFor(data)
    })

    client.getAttribute("java.lang:type=MemoryPool,name=PS Old Gen", "Usage", (data: Dynamic) => {
      heapUsageBarsData(0) = percentFor(data)
    })

    // NON_HEAP

    client.getAttribute("java.lang:type=MemoryPool,name=Code Cache", "Usage", (data: Dynamic) => {
      offheapUsageBarsData(1) = percentFor(data)
    })

    client.getAttribute("java.lang:type=MemoryPool,name=Compressed Class Space", "Usage", (data: Dynamic) => {
      offheapUsageBarsData(2) = percentFor(data)
    })

    client.getAttribute("java.lang:type=MemoryPool,name=Metaspace", "Usage", (data: Dynamic) => {
      //HAAAAAAAAACK
      // :-(
      val used = data.getSync("committed").toString.toDouble
      val max = used * 5
      offheapUsageBarsData(0) = math.abs(math.ceil((used / max) * 100.0D))
    })
  }

  def renderScreen(): Unit = {
    heapUsageLine.setData(Array[String](" "), heapUsagePercentData)
    loadedClassesLine.setData(Array[String](" "), loadedClassesData)
    threadsLine.setData(Array[String](" "), threadsData)

    heapUsageBars.setData(js.Dynamic.literal(titles = Array[String]("OldGen", "Eden", "Survivor"), data = heapUsageBarsData))
    offHeapUsageBars.setData(js.Dynamic.literal(titles = Array[String]("Meta", "Cache", "Compr"), data = offheapUsageBarsData))

    screen.render()
  }

}

