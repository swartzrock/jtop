package jtop

case class Client(host: String, port: Int)

object Client {
  import scala.language.implicitConversions
  import scala.scalajs.js

  //{ host: "localhost", port: 1000 }
  implicit def toLiteral(client: Client): js.Dynamic =
    js.Dynamic.literal(host = client.host, port = client.port)
}
