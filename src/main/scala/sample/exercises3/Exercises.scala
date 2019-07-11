package sample.exercises3

import java.util.TimeZone

object Exercises {
    def functional1(): Unit = {
      println(TimeZone.getAvailableIDs.filter(_.contains("/")).map(_.split("/").tail).map(_(0)).toList)
    }
}
