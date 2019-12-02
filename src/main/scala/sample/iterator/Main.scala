package sample.iterator

import sample.iterator.Iterator.iterate
import sample.iterator.PointMass.Vector2D

import scala.collection.mutable.ArrayBuffer

object Main {
  def main(args: Array[String]): Unit = {
    //    val objects: Seq[PointMass] = Seq(
    //      PointMass(
    //        mass = 100000,
    //        position = Vector2D(0, 0)
    //      ),
    //      PointMass(
    //        mass = 1000,
    //        position = Vector2D(-300, -300),
    //        velocity = Vector2D(-11.5, 11)
    //      ),
    //      PointMass(
    //        mass = 11,
    //        position = Vector2D(-315, -315),
    //        velocity = Vector2D(-15.5, 16)
    //      ),
    //      PointMass(
    //        mass = 400,
    //        position = Vector2D(200, -200),
    //        velocity = Vector2D(-15, -11)
    //      ),
    //      PointMass(
    //        mass = 50,
    //        position = Vector2D(40, 40),
    //        velocity = Vector2D(-43, 35)
    //      ),
    //      PointMass(
    //        mass = 500,
    //        position = Vector2D(700, 0),
    //        velocity = Vector2D(0, 12)
    //      ),
    //      PointMass(
    //        mass = 150,
    //        position = Vector2D(725, -25),
    //        velocity = Vector2D(3, 14)
    //      ),
    //      PointMass(
    //        mass = 500,
    //        position = Vector2D(-500, 70),
    //        velocity = Vector2D(20, 0)
    //      )
    //    )
    val n: Int = 5

    val objects: ArrayBuffer[PointMass] = ArrayBuffer(
      PointMass(
        mass = 1000000,
        position = Vector2D(-100, 10.05 + 30.15 * n * 4),
        velocity = Vector2D(1000, 0),
        width = Some(100)
      )
    )


    Range(0, n).foreach { i =>
      Range(0, n * 4).foreach { j =>
        objects.append(
          PointMass(
            mass = 100000,
            position = Vector2D(0 + 18.1 * i, (20.1 + 30.15 * i - 40.2 * (i % 2)) % 60.3 + 60.3 * j),
            width = Some(20)
          ),
          PointMass(
            mass = 100000,
            position = Vector2D(0 + 18.1 * i, (30.15 * i - 20.1 * (i % 2)) % 60.3 + 60.3 * j + 40.2),
            width = Some(20)
          )
        )
      }
    }

    iterate(objects.toSeq, 0.00001, 1000000000, Visuals(objects.toSeq, 0))

  }
}
