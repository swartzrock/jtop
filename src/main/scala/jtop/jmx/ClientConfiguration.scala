package jtop.jmx

import scala.scalajs.js

trait ClientConfiguration extends js.Object {
  val host: String
  val port: Int
}

object ClientConfiguration {
  def apply(host: String, port: Int): ClientConfiguration =
    js.Dynamic.literal(host = host, port = port).asInstanceOf[ClientConfiguration]
}
