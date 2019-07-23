package sample.hangman

import scala.collection.mutable.ArrayBuffer

object Hangman {
  def game(word: String, state: Array[Int] = Array(5, 1), guesses: ArrayBuffer[Char] = ArrayBuffer[Char]()): Unit = {
    println(s"You have ${state(0)} lives")
    println(s"You have made ${state(1)} guesses so far which are ${guesses.toIndexedSeq.mkString(sep = " ")}")
    printHiddenWord(word, guesses)
    askForInput() match {
      case x if guesses.contains(x) =>
        println(s"You have already guessed $x try another letter")
      case x if word.toCharArray.contains(x) =>
        println(s"Letter $x was a good guess, nice")
        guesses.append(x)
        state(1) += 1
      case x if !word.toCharArray.contains(x) =>
        println(s"Letter $x was a baad choice man, unlucky")
        guesses.append(x)
        state(0) += -1
        state(1) += 1
    }
    didYouWin(word, state, guesses) match {
      case true =>
        println(s"\nYou win, get out now \nThe word was $word")
        System.exit(0)
      case false =>
        println("")
        game(word, state, guesses)
      case _ =>
        println("You lose, git gud")
        System.exit(0)
    }
  }

  def didYouWin(word: String, state: Array[Int], guesses: ArrayBuffer[Char]): Any = {
    if (state(0) > 0) word.toCharArray.toSet.subsetOf(guesses.toSet)
  }

  def printHiddenWord(word: String, guesses: ArrayBuffer[Char]): Unit = {
    word.toCharArray.foreach({
      _ match {
        case x if guesses.contains(x) => print(x + " ")
        case x if !guesses.contains(x) => print("_ ")
      }
    })
    println(" ")
  }

  def askForInput(): Char = {
    println("Please input a letter")
    val input = scala.io.StdIn.readLine()
    input.toLowerCase() match {
      case x if x.length != 1 =>
        println("Please input a single letter")
        askForInput()
      case x if x.length == 1 && !isLetter(x) =>
        println("This is not a letter, try again")
        askForInput()
      case x if x.length == 1 && isLetter(x) =>
        println(s"You have input $x")
        x.charAt(0)
    }
  }

  def isLetter(x: String): Boolean = {
    if (x.charAt(0) >= 97 && x.charAt(0) <= 122) true
    else false
  }
}
