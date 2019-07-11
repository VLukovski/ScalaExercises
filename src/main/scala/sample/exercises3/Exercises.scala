package sample.exercises3

import java.util.{Calendar, TimeZone}

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import util.control.Breaks._

object Exercises {
  def functional1(): Unit = {
    println(TimeZone.getAvailableIDs.filter(_.contains("/")).map(_.split("/").tail).map(_ (0)).toList)
  }
//this is kinda slow
  def primes(bound: Long): Unit = {
    var count: Integer = 10
    var num: Long = 1L
    val t1 = System.nanoTime
    for (i <- 2L to bound) {
      breakable {
        if (i % 2 == 0 || i % 3 == 0 || i % 5 == 0 || i % 7 == 0 || i % 11 == 0 || i % 13 == 0 || i % 17 == 0 || i % 19 == 0 || i % 23 == 0 || i % 29 == 0) break
        while (num <= i / 29) {
          if (i % num == 0) {
            //println(i.toString + " " + num)
            break
          }
          num += 2L
        }
        count += 1
        println(count + " " + i)
      }
      num = 3L
    }

    println((System.nanoTime - t1) / 1e9d)
  }
//this is super slow
  def primes2(bound: Int): Unit = {
    var primes = Array.fill(bound/10){2}
    val t1 = System.nanoTime
    var count = 0
    (2 to bound).foreach(i =>
      if (!primes.distinct.exists(i % _ == 0)) {
        primes(count) = i
        count += 1
        println(i)
      })
    println((System.nanoTime - t1) / 1e9d)
    println(primes.length)
  }
}
