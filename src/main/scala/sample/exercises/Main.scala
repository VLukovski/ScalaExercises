package sample.exercises

object Main {
  def main (args: Array[String]): Unit = {
    Exercises.helloWorld()
    Exercises.assignHelloWorld()
    Exercises.acceptString("Help")
    println(Exercises.returnHelloWorld())
    println(Exercises.inferType(true))
    println(Exercises.inferType("string"))
    println(Exercises.inferType(2.2))
    println(Exercises.inferType(1))

  }
}
