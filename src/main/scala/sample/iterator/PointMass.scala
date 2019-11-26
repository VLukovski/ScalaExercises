package sample.iterator

import sample.iterator.PointMass.{Acceleration, Position, Velocity}

case class PointMass(mass: Double, position: Position, velocity: Velocity = Velocity(0, 0), accel: Acceleration = Acceleration(0, 0)) {

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

  case class Acceleration(x: Double, y: Double) {
    def +(that: Acceleration) =
      Acceleration(this.x + that.x, this.y + that.y)

    def -(that: Acceleration) =
      Acceleration(this.x - that.x, this.y - that.y)

    def /(that: Double) =
      Acceleration(this.x / that, this.y / that)

    def flip =
      Acceleration(-this.x, -this.y)
  }

}