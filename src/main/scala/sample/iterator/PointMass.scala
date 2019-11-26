package sample.iterator

import sample.iterator.PointMass.{Force, Position, Velocity}

case class PointMass(mass: Double, position: Position, velocity: Velocity = Velocity(0, 0), force: Force = Force(0, 0)) {

}

object PointMass {

  case class Position(x: Double, y: Double) {
    def +(that: Position) =
      Position(this.x + that.x, this.y + that.y)
  }

  case class Velocity(x: Double, y: Double) {
    def +(that: Velocity) =
      Velocity(this.x + that.x, this.y + that.y)
  }

  case class Force(x: Double, y: Double) {
    def +(that: Force) =
      Force(this.x + that.x, this.y + that.y)

    def flip =
      Force(-this.x, -this.y)
  }

}