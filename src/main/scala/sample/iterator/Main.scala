package sample.iterator

import sample.iterator
import sample.iterator.Iterator.iterate
import sample.iterator.PointMass.{Position, Velocity}

object Main {
  def main(args: Array[String]): Unit = {
    val objects: Seq[PointMass] = Seq(
      PointMass(
        mass = 100000,
        position = Position(0, 0)
      ),
      PointMass(
        mass = 1000,
        position = Position(-300, -300),
        velocity = Velocity(-10.5, 11)
      ),
      PointMass(
        mass = 10,
        position = Position(-315, -315),
        velocity = Velocity(-15.5, 16)
      ),
      PointMass(
        mass = 400,
        position = Position(200, -200),
        velocity = Velocity(-15, -10)
      ),
      PointMass(
        mass = 50,
        position = Position(40, 40),
        velocity = Velocity(-43, 35)
      ),
      PointMass(
        mass = 500,
        position = Position(700, 0),
        velocity = Velocity(0, 12)
      ),
      PointMass(
        mass = 150,
        position = Position(725, -25),
        velocity = Velocity(3, 14)
      ),
      PointMass(
        mass = 500,
        position = Position(-500, 70),
        velocity = Velocity(20, 0)
      )
    )


    iterate(objects, 0.0001, 1000000000, Visuals(objects, 0))

  }
}
