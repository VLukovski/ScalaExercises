package sample.iterator

import sample.iterator.PointMass.Vector2D

import scala.collection.mutable.ArrayBuffer

case class Iterator(objects: ArrayBuffer[PointMass], timeStep: Double, visuals: Visuals, var distances: Seq[Seq[Double]] = Seq(Seq()), var pause: Boolean = false, var gravity: Boolean = false) {

  def iterate(): Unit = {
    while (true) {
      while (pause) {}
      calcDistances()
      accelCalc()
      collisionCalc()
      velocityPositionCalc()
    }
  }

  private def calcDistances(): Unit = {
    distances = objects.zipWithIndex.map { case (obj1, index) =>
      objects.drop(index + 1).map { obj2 =>
        obj1.position.distance(obj2.position)
      }.toSeq
    }.toSeq
  }

  private def accelCalc(): Unit = {
    if (gravity) {
      objects.mapInPlace(_.copy(accel = Vector2D(0, 0)))
      objects.zipWithIndex.foreach { case (obj1, index) =>
        objects.drop(index + 1).zip(distances(index)).foreach { case (obj2, dist) =>
          val diff = Vector2D(
            (-1 * obj2.mass * obj1.mass * (-obj1.position.x + obj2.position.x)) / (dist * dist * dist),
            (-1 * obj2.mass * obj1.mass * (-obj1.position.y + obj2.position.y)) / (dist * dist * dist)
          )
          obj2.accel += diff / obj2.mass
          obj1.accel -= diff / obj1.mass
        }
      }
    }
  }

  private def collisionCalc(): Unit = {
      objects.zipWithIndex.foreach { case (obj1, i) =>
        objects.drop(i + 1).zip(distances(i)).foreach { case (obj2, dist) =>
          if (obj1.width + obj2.width > dist * 2) {
            val newVel1: Vector2D = obj1.velocity - (obj1.position - obj2.position) * 2 * obj2.mass / (obj1.mass + obj2.mass) * (obj1.velocity - obj2.velocity).dot(obj1.position - obj2.position) / (dist * dist)
            val newVel2: Vector2D = obj2.velocity - (obj2.position - obj1.position) * 2 * obj1.mass / (obj2.mass + obj1.mass) * (obj2.velocity - obj1.velocity).dot(obj2.position - obj1.position) / (dist * dist)
            val minTransDist: Vector2D = (obj1.position - obj2.position) * (obj1.width / 2 + obj2.width / 2 - dist) / dist
            val inverseMass1: Double = 1 / obj1.mass
            val inverseMass2: Double = 1 / obj2.mass

            objects(i).velocity = newVel1
            objects(i).position = obj1.position + minTransDist * (inverseMass1 / (inverseMass1 + inverseMass2))
            objects(objects.indexOf(obj2)).velocity = newVel2
            objects(objects.indexOf(obj2)).position = obj2.position - minTransDist * (inverseMass2 / (inverseMass1 + inverseMass2))

        }
      }
    }
  }

  private def velocityPositionCalc(): Unit = {
    objects.foreach { obj =>
      obj.velocity = Vector2D(
        obj.velocity.x + obj.accel.x * timeStep,
        obj.velocity.y + obj.accel.y * timeStep
      )
      obj.position = Vector2D(
        obj.position.x + obj.velocity.x * timeStep,
        obj.position.y + obj.velocity.y * timeStep
      )
    }
  }
}
