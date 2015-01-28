package jtop.jmx

import scala.scalajs.js

trait Client extends js.Object {

  def connect() = ???
  def disconnect() = ???

  def getAttribute(mbean: js.String, attribute: js.String, callback: js.Function) = ???

  def on(event: js.String, callback: js.Function) = ???

}
