package sample.iterator

import sample.iterator.PointMass.{Acceleration, Position, Velocity}

object Iterator {

  @scala.annotation.tailrec
  def iterate(objects: Seq[PointMass], timeStep: Double, times: Int = 1, visuals: Visuals, gravity: Double = 1.0): Seq[PointMass] = {
    if (times > 0) {
      visuals.objects = objects
      iterate(
        velocityPositionCalc(
          collisionCalc(
            accelCalc(
              objList = objects.map(_.copy(accel = Acceleration(0, 0))),
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
        Math.max(obj1.position.distance(obj2.position), 2)
      }
    }
  }

  @scala.annotation.tailrec
  private def accelCalc(objList: Seq[PointMass], returnObjList: Seq[PointMass] = Seq[PointMass](), distanceList: Seq[Seq[Double]], gravConst: Double): (Seq[PointMass], Seq[Seq[Double]]) = {
    if (objList.nonEmpty) {
      val diffs: Seq[Acceleration] = objList.tail.zip(distanceList(returnObjList.length)).map { case (obj, dist) =>
        Acceleration(
          (-gravConst * obj.mass * objList.head.mass * (-objList.head.position.x + obj.position.x)) / (dist * dist * dist),
          (-gravConst * obj.mass * objList.head.mass * (-objList.head.position.y + obj.position.y)) / (dist * dist * dist)
        )
      }

      val updatedObjList: Seq[PointMass] = objList.tail.zip(diffs).map { case (obj, diff) =>
        obj.copy(accel = obj.accel + diff)
      }

      val updatedHead: PointMass = objList.head.copy(accel = (objList.head.accel - diffs.foldLeft(Acceleration(0, 0))(_ + _)) / objList.head.mass)

      accelCalc(updatedObjList, returnObjList.appended(updatedHead), distanceList, gravConst)
    }
    else (returnObjList, distanceList)
  }

  private def collisionCalc(lists: (Seq[PointMass], Seq[Seq[Double]]), timeStep: Double): Seq[PointMass] = {
    val array = lists._1.toBuffer
    lists._1.zipWithIndex.foreach { case (obj1, i) =>
      lists._1.drop(i + 1).zip(lists._2(i)).foreach { case (obj2, dist) =>
        if (obj1.width + obj2.width >= dist * 2 && obj1.velocity ) {
          //          val collisionPoint: Position = Position(
          //            ((obj1.position.x * obj2.width) + (obj2.position.x * obj1.width)) / (obj1.width + obj2.width),
          //            ((obj1.position.y * obj2.width) + (obj2.position.y * obj1.width)) / (obj1.width + obj2.width)
          //          )

          val newVel1: Velocity = Velocity(
            (obj1.velocity.x * (obj1.mass - obj2.mass) + (2 * obj2.mass * obj2.velocity.x)) / (obj1.mass + obj2.mass),
            (obj1.velocity.y * (obj1.mass - obj2.mass) + (2 * obj2.mass * obj2.velocity.y)) / (obj1.mass + obj2.mass)
          )
          val newVel2: Velocity = Velocity(
            (obj2.velocity.x * (obj2.mass - obj1.mass) + (2 * obj1.mass * obj1.velocity.x)) / (obj2.mass + obj1.mass),
            (obj2.velocity.y * (obj2.mass - obj1.mass) + (2 * obj1.mass * obj1.velocity.y)) / (obj2.mass + obj1.mass)
          )

          array(i) = array(i).copy(velocity = obj1.velocity + newVel1)
          array(lists._1.indexOf(obj2)) = obj2.copy(velocity = obj2.velocity + newVel2)
        }
      }
    }
    array.toSeq
  }

  private def velocityPositionCalc(objList: Seq[PointMass], timeStep: Double): Seq[PointMass] = {
    objList.map { obj =>
      obj.copy(
        velocity = Velocity(
          obj.velocity.x + obj.accel.x * timeStep,
          obj.velocity.y + obj.accel.y * timeStep
        ),
        position = Position(
          obj.position.x + obj.velocity.x * timeStep,
          obj.position.y + obj.velocity.y * timeStep
        )
      )
    }
  }
}
