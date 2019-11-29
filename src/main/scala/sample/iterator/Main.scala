package sample.iterator

import sample.iterator.Iterator.iterate
import sample.iterator.PointMass.Vector2D

object Main {
  def main(args: Array[String]): Unit = {
    val objects: Seq[PointMass] = Seq(
      PointMass(
        mass = 100000,
        position = Vector2D(0, 0)
      ),
      PointMass(
        mass = 1000,
        position = Vector2D(-300, -300),
        velocity = Vector2D(-10.5, 11)
      ),
      PointMass(
        mass = 10,
        position = Vector2D(-315, -315),
        velocity = Vector2D(-15.5, 16)
      ),
      PointMass(
        mass = 400,
        position = Vector2D(200, -200),
        velocity = Vector2D(-15, -10)
      ),
      PointMass(
        mass = 50,
        position = Vector2D(40, 40),
        velocity = Vector2D(-43, 35)
      ),
      PointMass(
        mass = 500,
        position = Vector2D(700, 0),
        velocity = Vector2D(0, 12)
      ),
      PointMass(
        mass = 150,
        position = Vector2D(725, -25),
        velocity = Vector2D(3, 14)
      )
      //      ,
      //      PointMass(
      //        mass = 500,
      //        position = Position(-500, 70),
      //        velocity = Velocity(20, 0)
      //      )
    )


    iterate(objects, 0.0001, 1000000000, Visuals(objects, 0))

  }
}
