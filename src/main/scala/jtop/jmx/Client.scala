package jtop.jmx

import scala.scalajs.js

trait Client extends js.Object {

  def connect(): Unit = ???
  def disconnect(): Unit = ???

  def getAttribute(mbean: js.String, attribute: js.String, callback: js.Function): Unit = ???

  def on(event: js.String, callback: js.Function): Unit = ???

}
