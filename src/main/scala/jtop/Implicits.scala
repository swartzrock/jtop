package jtop

import scala.scalajs.js

object Implicits {
  import upickle._
  import scala.language.implicitConversions

  implicit def toJsAny[T : Writer](expr: T): js.Any =
    json.writeJs(writeJs(expr)).asInstanceOf[js.Any]
}
