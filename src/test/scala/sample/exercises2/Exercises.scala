package sample.exercises2

import java.util
import java.util.TimeZone

object Exercises {
  def strings1(string: String, num: Int): Unit = {
    println(string.substring(string.length - num))
  }

  def strings2(str1: String, str2: String, char1: Char, char2: Char): Unit = {
    println((str1 + str2).replace(char1, char2))
  }

  def sum(int1: Int, int2: Int): Unit = {
    println(int1 + int2)
  }

  def sumOrMult(int1: Int, int2: Int, condition: Boolean): Unit = {
    if (condition) {
      println(int1 + int2)
    }
    else {
      println(int1 * int2)
    }
  }

  def moreConditions(int1: Int, int2: Int, condition: Boolean): Unit = {
    if (int1 == 0 || int2 == 0) {
      println(int1 + int2)
    }
    else if (condition) {
      println(int1 + int2)
    }
    else {
      println(int1 * int2)
    }
  }

  def printString(string: String, count: Int): Unit = {
    for (i <- 1 to count) println(string)
  }

  def printSquare(string: String, size: Int): Unit = {
    for (i <- 1 to size) println(string * size)
  }

  def buzzFizz(str1: String, str2: String, num: Int): Unit = {
    for (i <- 1 to num) {
      if (i % 15 == 0) {
        println(str1 + str2 + " ")
      }
      else if (i % 3 == 0) {
        println(str1 + " ")
      }
      else if (i % 5 == 0) {
        println(str2 + " ")
      }
      else {
        println(i + " ")
      }
    }
  }

  def printStringRecursive(string: String, count: Int): Unit = {
    println(string)
    if (count > 0) printStringRecursive(string, count - 1)
  }

  def printSquareRecursive(string: String, size: Int, count: Int = 0): Unit = {
    println(string * (size + count))
    if (size > 0) printSquareRecursive(string, size - 1, count + 1)
  }

  def buzzFizzRecursive(str1: String, str2: String, num: Int, count: Int = 1): Unit = {
    if (count % 15 == 0) {
      println(str1 + str2 + " ")
    }
    else if (count % 3 == 0) {
      println(str1 + " ")
    }
    else if (count % 5 == 0) {
      println(str2 + " ")
    }
    else {
      println(count + " ")
    }
    if (num > 1) buzzFizzRecursive(str1, str2, num - 1, count + 1)
  }

  def buzzFizzRecursiveCases(str1: String, str2: String, num: Int, count: Int = 1): Unit = {
    (count % 3 == 0, count % 5 == 0) match {
      case (true, true) => println(str1 + str2 + " ")
      case (true, false) => println(str1 + " ")
      case (false, true) => println(str2 + " ")
      case _ => println(count + " ")
    }
    if (num > 1) buzzFizzRecursiveCases(str1, str2, num - 1, count + 1)
  }

  def patternMatch2(pair: Any): Unit = {
    pair match {
      case pair: List[Int] => println(pair.reverse)
      case pair: Array[Int] => {
        pair.reverse.foreach(print)
        println()
      }
      case pair: Tuple2[Int, Int] => println((pair._2, pair._1))
      case _ => println("Wrong input")
    }
  }

//  def functional1(): Unit = {
//    println(TimeZone.getAvailableIDs.foreach
//  }
}
