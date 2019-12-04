package sample.iterator

import sample.iterator.PointMass.Vector2D

case class PointMass(mass: Double, position: Vector2D, velocity: Vector2D, accel: Vector2D, width: Double, isMovable: Boolean)

object PointMass {

  def apply(mass: Double, position: Vector2D, velocity: Vector2D = Vector2D(0, 0), accel: Vector2D = Vector2D(0, 0), width: Option[Double] = None, isMovable: Boolean = true): PointMass =
    this (mass,
      position,
      velocity,
      accel,
      if (width.isEmpty) Math.pow(mass, 1.0 / 3) + 10
      else width.get,
      isMovable
    )

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
