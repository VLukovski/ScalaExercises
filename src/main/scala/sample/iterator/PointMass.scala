package sample.iterator

import sample.iterator.PointMass.Vector2D

case class PointMass(mass: Double, position: Vector2D, velocity: Vector2D = Vector2D(0, 0), accel: Vector2D = Vector2D(0, 0)) {
  val width: Double = Math.pow(this.mass, 1.0 / 3) + 10

}

object PointMass {

  case class Vector2D(x: Double, y: Double) {
    def +(that: Vector2D) =
      Vector2D(this.x + that.x, this.y + that.y)

    def -(that: Vector2D) =
      Vector2D(this.x - that.x, this.y - that.y)

    def /(that: Double) =
      Vector2D(this.x / that, this.y / that)

    def *(that: Double) =
      Vector2D(this.x * that, this.y * that)

    def dot(that: Vector2D): Double =
      this.x * that.x + this.y * that.y

    def distance(that: Vector2D): Double =
      Math.sqrt((this.x - that.x) * (this.x - that.x) + (this.y - that.y) * (this.y - that.y))
  }

}
