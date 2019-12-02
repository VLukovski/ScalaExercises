package sample.iterator

import sample.iterator.PointMass.Vector2D

object Iterator {

  @scala.annotation.tailrec
  def iterate(objects: Seq[PointMass], timeStep: Double, times: Int = 1, visuals: Visuals, gravity: Double = 1.0): Seq[PointMass] = {
    if (times > 0) {
      visuals.objects = objects
      iterate(
        velocityPositionCalc(
          collisionCalc(
            accelCalc(
              objList = objects.map(_.copy(accel = Vector2D(0, 0))),
              distanceList = calcDistances(objects),
              gravConst = gravity
            ),
            timeStep = timeStep
          ),
          timeStep
        ),
        timeStep,
        times - 1,
        visuals
      )
    } else objects
  }

  private def calcDistances(objList: Seq[PointMass]): Seq[Seq[Double]] = {
    objList.zipWithIndex.map { case (obj1, index) =>
      objList.drop(index + 1).map { obj2 =>
        obj1.position.distance(obj2.position)
      }
    }
  }

  //  @scala.annotation.tailrec
  private def accelCalc(objList: Seq[PointMass], returnObjList: Seq[PointMass] = Seq[PointMass](), distanceList: Seq[Seq[Double]], gravConst: Double): (Seq[PointMass], Seq[Seq[Double]]) = {
    //    if (objList.nonEmpty) {
    //      val diffs: Seq[Vector2D] = objList.tail.zip(distanceList(returnObjList.length)).map { case (obj, dist) =>
    //        Vector2D(
    //          (-gravConst * obj.mass * objList.head.mass * (-objList.head.position.x + obj.position.x)) / (dist * dist * dist),
    //          (-gravConst * obj.mass * objList.head.mass * (-objList.head.position.y + obj.position.y)) / (dist * dist * dist)
    //        )
    //      }
    //
    //      val updatedObjList: Seq[PointMass] = objList.tail.zip(diffs).map { case (obj, diff) =>
    //        obj.copy(accel = obj.accel + diff)
    //      }
    //
    //      val updatedHead: PointMass = objList.head.copy(accel = (objList.head.accel - diffs.foldLeft(Vector2D(0, 0))(_ + _)) / objList.head.mass)
    //
    //      accelCalc(updatedObjList, returnObjList.appended(updatedHead), distanceList, gravConst)
    //    }
    //    else (returnObjList, distanceList)
    (objList, distanceList)
  }

  private def collisionCalc(lists: (Seq[PointMass], Seq[Seq[Double]]), timeStep: Double): Seq[PointMass] = {
    val array = lists._1.toBuffer
    lists._1.zipWithIndex.foreach { case (obj1, i) =>
      lists._1.drop(i + 1).zip(lists._2(i)).foreach { case (obj2, dist) =>
        if (obj1.width + obj2.width > dist * 2) {
          val newVel1: Vector2D = obj1.velocity - (obj1.position - obj2.position) * 2 * obj2.mass / (obj1.mass + obj2.mass) * (obj1.velocity - obj2.velocity).dot(obj1.position - obj2.position) / (dist * dist)
          val newVel2: Vector2D = obj2.velocity - (obj2.position - obj1.position) * 2 * obj1.mass / (obj2.mass + obj1.mass) * (obj2.velocity - obj1.velocity).dot(obj2.position - obj1.position) / (dist * dist)
          val minTransDist: Vector2D = (obj1.position - obj2.position) * (obj1.width / 2 + obj2.width / 2 - dist) / dist
          val inverseMass1: Double = 1 / obj1.mass
          val inverseMass2: Double = 1 / obj2.mass

          array(i) = array(i).copy(velocity = newVel1, position = obj1.position + minTransDist * (inverseMass1 / (inverseMass1 + inverseMass2)))
          array(lists._1.indexOf(obj2)) = obj2.copy(velocity = newVel2, position = obj2.position - minTransDist * (inverseMass2 / (inverseMass1 + inverseMass2)))
        }
      }
    }
    array.toSeq
  }

  private def velocityPositionCalc(objList: Seq[PointMass], timeStep: Double): Seq[PointMass] = {
    objList.map { obj =>
      obj.copy(
        velocity = Vector2D(
          obj.velocity.x + obj.accel.x * timeStep,
          obj.velocity.y + obj.accel.y * timeStep
        ),
        position = Vector2D(
          obj.position.x + obj.velocity.x * timeStep,
          obj.position.y + obj.velocity.y * timeStep
        )
      )
    }
  }
}
