package sample.exercises

object Exercises {
  def helloWorld()= println("Hello World!")
  def assignHelloWorld(): Unit = {
    val helloWorld = "Hello World!"
    println(helloWorld)
  }
  def acceptString(str: String): Unit = println(str)
  def returnHelloWorld(): String = "Hello World!"
  def inferType(stuff: Any) = stuff
}
