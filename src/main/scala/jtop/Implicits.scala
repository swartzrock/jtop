package jtop

object Implicits {
  implicit class Pickler[T](val expr: T) extends AnyVal {
    def write(implicit writer: upickle.Writer[T]) =
      upickle.write(expr)
  }
}
