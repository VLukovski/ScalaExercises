package sample.iterator

import sample.iterator.Iterator.iterate
import sample.iterator.PointMass.{Position, Velocity}

object Main {
  def main(args: Array[String]): Unit = {
        val objects: Seq[PointMass] = Seq(
          PointMass(
            mass = 100000,
            position = Position(0, 0),
            velocity = Velocity(0.17, -0.07)
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
            velocity = Velocity(-12, -11)
          ),
          PointMass(
            mass = 50,
            position = Position(70, 70),
            velocity = Velocity(-26, 25)
          )
        )
//    val objects = Seq(
//      PointMass(
//        mass = 5000,
//        position = Position(-100,-100),
//        velocity = Velocity(0,0)
//      ),
//      PointMass(
//        mass = 3000,
//        position = Position(200,-100),
//        velocity = Velocity(0,0)
//      ),
//      PointMass(
//        mass = 4000,
//        position = Position(-100,300),
//        velocity = Velocity(0,0)
//      )
//    )

    iterate(objects, 0.00005, 1000000000, Visuals(objects))

  }
}
