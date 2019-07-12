package sample.cron

import java.nio.file.{Files, Paths}

import scala.collection.mutable.ListBuffer
import scala.io.Source

object Cron extends App {

  def getInputs(): List[String] = {
    println("Please input current time in HH:MM format")
    val timeIn = scala.io.StdIn.readLine()
    if (!timeIn.contains(":")) {
      println("Use the correct format")
      System.exit(1)
    }
    val hours = timeIn.split(":")(0)
    val minutes = timeIn.split(":")(1)
    if (hours.toInt > 23 || hours.toInt < 0 || minutes.toInt > 59 || minutes.toInt < 0) {
      println("Input a real time")
      System.exit(1)
    }

    println("Please input the config file path")
    val config = scala.io.StdIn.readLine()
    if (!Files.exists(Paths.get(config))) {
      println("File does not exist")
      System.exit(1)
    }
    List(timeIn, config)
  }

  def getTime(hours: String, minutes: String, line: String): Unit = {
    val elements = line.split(" ")
    val output = ListBuffer[String]("", "")
    if (elements(2).contains("daily")) {
      output(0) = elements(1) + ":" + elements(0)
      if (hours.toInt > elements(1).toInt) output(1) = "tomorrow"
      else if (hours.toInt == elements(1).toInt && minutes.toInt > elements(0).toInt) output(1) = "tomorrow"
      else output(1) = "today"
    }
    else if (elements(2).contains("hourly")) {
      if (hours.toInt == 23) {
        output(0) = "00" + ":" + elements(0)
        output(1) = "tomorrow"
      }
      else if (minutes.toInt <= elements(0).toInt) {
        output(0) = hours + ":" + elements(0)
        output(1) = "today"
      }
      else {
        output(0) = (hours.toInt + 1).toString + ":" + elements(0)
        output(1) = "today"
      }
    }
    else if (elements(2).contains("minute")) {
      output(0) = hours + ":" + minutes
      output(1) = "today"
    }
    else if (elements(2).contains("times")) {
      if (hours.toInt == elements(1).toInt + 1 && minutes.toInt < elements(0).toInt) {
        output(0) = hours + ":" + minutes
        output(1) = "today"
      }
      else if (hours.toInt == elements(1).toInt && minutes.toInt >= elements(0).toInt) {
        output(0) = hours + ":" + minutes
        output(1) = "today"
      }
      else if ((hours.toInt == elements(1).toInt + 1 && minutes.toInt >= elements(0).toInt) || hours.toInt > elements(1).toInt + 1) {
        output(0) = elements(1) + ":" + elements(0)
        output(1) = "tomorrow"
      }
      else {
        output(0) = elements(1) + ":" + elements(0)
        output(1) = "today"
      }
    }
    if (output(0).split(":")(0).length == 1) output(0) = "0" + output(0)
    if (output(0).split(":")(1).length == 1) output(0) = output(0).substring(0, 3) + "0" + output(0).substring(3)
    println(output(0) + " " + output(1) + " " + elements(2))
  }

  def checkConfig(line: String): Boolean = {
    val lines = line.split(" ")
    if (lines.length != 3 || (!lines(2).contains("daily") && !lines(2).contains("hourly") && !lines(2).contains("times") && !lines(2).contains("minute"))) false
    else true
  }

  def getFile(config: String): List[String] = {
    Source.fromFile(config).getLines.toList
  }

  val inputs = getInputs()
  val file = getFile(inputs(1))
  file.filter(checkConfig(_)).foreach(getTime(inputs(0).split(":")(0), inputs(0).split(":")(1), _))
}
