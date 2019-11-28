package sample.iterator

import sample.iterator.PointMass.{Acceleration, Position, Velocity}

case class PointMass(mass: Double, position: Position, velocity: Velocity = Velocity(0, 0), accel: Acceleration = Acceleration(0, 0)) {
  val width: Double = Math.pow(this.mass, 1.0 / 3) + 10

}

object PointMass {

  case class Position(x: Double, y: Double) {
    def +(that: Position) =
      Position(this.x + that.x, this.y + that.y)

    def +(that: Velocity) =
      Position(this.x + that.x, this.y + that.y)

    def -(that: Position) =
      Position(this.x - that.x, this.y - that.y)

    def distance(that: Position): Double =
      Math.sqrt((this.x - that.x) * (this.x - that.x) + (this.y - that.y) * (this.y - that.y))
  }

  case class Velocity(x: Double, y: Double) {
    def +(that: Velocity) =
      Velocity(this.x + that.x, this.y + that.y)

    def -(that: Velocity) =
      Velocity(this.x - that.x, this.y - that.y)

    def /(that: Double) =
      Velocity(this.x / that, this.y / that)

    def *(that: Double) =
      Velocity(this.x * that, this.y * that)

    def dot(that: Velocity): Double =
      this.x * that.x + this.y * that.y
  }

  case class Acceleration(x: Double, y: Double) {
    def +(that: Acceleration) =
      Acceleration(this.x + that.x, this.y + that.y)

    def -(that: Acceleration) =
      Acceleration(this.x - that.x, this.y - that.y)

    def /(that: Double) =
      Acceleration(this.x / that, this.y / that)
  }

}