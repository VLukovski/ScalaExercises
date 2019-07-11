package sample.exercises3

import java.util.TimeZone

object Exercises {
    def functional1(): Unit = {
      println(TimeZone.getAvailableIDs.map(_.split("/").tail).filter(_.length > 0).map(_(0)).toList)
      println(TimeZone.getAvailableIDs().toList)
    }
}
