package htop

case class Client(host: String, port: Int)

object Client {
  import scala.language.implicitConversions
  import scala.scalajs.js

  implicit def toLiteral(client: Client): js.Dynamic =
    js.Dynamic.literal(host = client.host, port = client.port)
}
