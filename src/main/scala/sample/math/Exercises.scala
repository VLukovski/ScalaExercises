package sample.math

import scala.collection.mutable.ArrayBuffer

object Exercises extends App {

  var num1 = 0.0
  var num2 = 1.0

  def pi: Unit = {
    num1 += 1.0 / num2
    num2 += 2.0
    num1 -= 1.0 / num2
    num2 += 2.0
    println(4 * num1 + " " + num2)
    pi
  }

  pi

}
