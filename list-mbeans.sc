import scala.collection.JavaConverters._
import java.lang.management.ManagementFactory

val server = ManagementFactory.getPlatformMBeanServer()
val instances = server.queryMBeans(null, null).asScala

instances.foreach { instance =>
  val objectName = instance.getObjectName
  println(objectName)
  server.getMBeanInfo(objectName).getAttributes.foreach(attr => println("- " + attr.getName))
}