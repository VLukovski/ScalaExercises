package sample.exercises3

import scala.collection.mutable.ListBuffer
import scala.collection.parallel.CollectionConverters._

object Exercises {

  def primes3(bound: Int): Unit = {
    val primes = ListBuffer(2)
    val t1 = System.nanoTime
    var nums = Range(3, bound, 2).toVector
    val boundSqrt = Math.sqrt(bound)
    var prime: Int = 3
    while (prime < boundSqrt) {
      prime = nums.head
      println("Prime: " + prime)
      primes.prepend(prime)
      nums = nums.par.filter(num => num % prime != 0).seq
    }
    println((System.nanoTime - t1) / 1e9d)
    println((primes.toList ++ nums.toList).length)
  }

}

