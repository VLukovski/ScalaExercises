package sample.exercises3

import java.util.TimeZone

import util.control.Breaks._

object Exercises {
  def functional1(): Unit = {
    println(TimeZone.getAvailableIDs.filter(_.contains("/")).map(_.split("/").tail).map(_.head).toList)
  }
//this is kinda slow
  def primes(bound: Long): Unit = {
    var count: Integer = 10
    var num: Long = 1L
    val t1 = System.nanoTime
    val primeList = List(2,3,5,7,11,13,17,19,23,29)
    for (i <- 2L to bound) {
      breakable {
        if (primeList.exists(i % _ == 0)) break
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
    var primes = List[Int]()
    val t1 = System.nanoTime
    var count = 0
    (2 to bound).foreach(i =>
      if (!primes.exists(i % _ == 0)) {
        primes = primes.appended(i)
        count += 1
        println(primes.length)
      })
    println((System.nanoTime - t1) / 1e9d)
    println(primes.length)
  }
}
