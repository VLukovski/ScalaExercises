package sample.iterator

import sample.iterator.PointMass.{Force, Position, Velocity}

case class PointMass(mass: Double, position: Position, velocity: Velocity, force: Force = Force(0, 0)) {

}

object PointMass {

  case class Position(x: Double, y: Double)

  case class Velocity(x: Double, y: Double)

  case class Force(x: Double, y: Double)

}