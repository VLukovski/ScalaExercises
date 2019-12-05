package sample.iterator

import sample.iterator.Iterator
import sample.iterator.PointMass.Vector2D

import scala.collection.mutable.ArrayBuffer

object Main {
  def main(args: Array[String]): Unit = {
//        val objects: ArrayBuffer[PointMass] = ArrayBuffer(
//          PointMass(
//            mass = 100000,
//            position = Vector2D(0, 0)
//          ),
//          PointMass(
//            mass = 1000,
//            position = Vector2D(-300, -300),
//            velocity = Vector2D(-11.5, 11)
//          ),
//          PointMass(
//            mass = 11,
//            position = Vector2D(-315, -315),
//            velocity = Vector2D(-15.5, 16)
//          ),
//          PointMass(
//            mass = 400,
//            position = Vector2D(200, -200),
//            velocity = Vector2D(-15, -11)
//          ),
//          PointMass(
//            mass = 50,
//            position = Vector2D(40, 40),
//            velocity = Vector2D(-43, 35)
//          ),
//          PointMass(
//            mass = 500,
//            position = Vector2D(700, 0),
//            velocity = Vector2D(0, 12)
//          ),
//          PointMass(
//            mass = 150,
//            position = Vector2D(725, -25),
//            velocity = Vector2D(3, 14)
//          ),
//          PointMass(
//            mass = 500,
//            position = Vector2D(-500, 70),
//            velocity = Vector2D(20, 0)
//          )
//        )

        val n: Int = 12
        val m: Int = 4

        val objects: ArrayBuffer[PointMass] = ArrayBuffer(
          PointMass(
            mass = 10000,
            position = Vector2D(-1000, 25.05 + 75.15 * n * m),
            velocity = Vector2D(40000, 0),
            width = Some(500)
          ),
          PointMass(
            mass = 5000,
            position = Vector2D(-1000, -2000 + 25.05 + 75.15 * n * m),
            velocity = Vector2D(40000, 40000),
            width = Some(150)
          ),
          PointMass(
            mass = 5000,
            position = Vector2D(-1000, 2000 + 25.05 + 75.15 * n * m),
            velocity = Vector2D(40000, -40000),
            width = Some(150)
          )
        )



        Range(0, n).foreach { i =>
          Range(0, n * m).foreach { j =>
            objects.append(
              PointMass(
                mass = 100,
                position = Vector2D(0 + 44.75 * i, (50.1 + 75.15 * i - 100.2 * (i % 2)) % 150.3 + 150.3 * j),
                width = Some(50)
              ),
              PointMass(
                mass = 100,
                position = Vector2D(0 + 44.75 * i, (75.15 * i - 50.1 * (i % 2)) % 150.3 + 150.3 * j + 100.2),
                width = Some(50)
              )
            )
          }
        }

//    val objects = ArrayBuffer(
//      PointMass(
//        1,
//        Vector2D(1015, 0),
//        Vector2D(0, 0),
//        width = Some(20)
//      ),
//      PointMass(
//        1000000000000.0,
//        Vector2D(1075, 0),
//        Vector2D(-3, 0),
//        width = Some(100)
//      ),
//      PointMass(
//        200000000000000000000000000000000000000000000000000000000000000000000000.0,
//        Vector2D(0, 0),
//        Vector2D(0, 0),
//        width = Some(2000)
//      )
//    )

    val visuals = Visuals(objects)
    val timeStep = 0.0001
    val world = Iterator(objects, timeStep, visuals, gravity = false)
    world.iterate()
  }
}
