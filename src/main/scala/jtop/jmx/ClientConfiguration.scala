package jtop.jmx

case class ClientConfiguration(host: String, port: Int)

object ClientConfiguration {
  import scala.language.implicitConversions
  import scala.scalajs.js

  // { host: "localhost", port: 1000 }
  implicit def toLiteral(client: ClientConfiguration): js.Dynamic =
    js.Dynamic.literal(host = client.host, port = client.port)
}
