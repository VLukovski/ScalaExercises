package sample.iterator

import sample.iterator.PointMass.{Acceleration, Position, Velocity}

object Iterator {

  @scala.annotation.tailrec
  def iterate(objects: Seq[PointMass], timeStep: Double, times: Int = 1, visuals: Visuals, gravity: Double = 1.0): Seq[PointMass] = {
    if (times > 0) {
      visuals.objects = objects
      iterate(
        accelCalc(objList = objects.map(_.copy(accel = Acceleration(0, 0))), distanceList = calcDistances(objects), gravConst = gravity).map { obj =>
          velocityCalc(
            positionCalc(
              obj,
              timeStep
            ),
            timeStep
          )
        },
        timeStep,
        times - 1,
        visuals
      )
    } else objects
  }

  private def calcDistances(objList: Seq[PointMass]): Seq[Seq[Double]] = {
    objList.zipWithIndex.map { case (obj1, index) =>
      objList.drop(index + 1).map { obj2 =>
        Math.max(Math.sqrt((obj1.position.x - obj2.position.x) * (obj1.position.x - obj2.position.x) + (obj1.position.y - obj2.position.y) * (obj1.position.y - obj2.position.y)), 2)
      }
    }
  }

  @scala.annotation.tailrec
  private def accelCalc(objList: Seq[PointMass], returnObjList: Seq[PointMass] = Seq[PointMass](), distanceList: Seq[Seq[Double]], gravConst: Double): Seq[PointMass] = {
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
    else returnObjList
  }

  private def positionCalc(obj: PointMass, timeStep: Double): PointMass = {
    obj.copy(position = Position(
      obj.position.x + obj.velocity.x * timeStep + obj.accel.x * Math.pow(timeStep, 2) / 2.0,
      obj.position.y + obj.velocity.y * timeStep + obj.accel.y * Math.pow(timeStep, 2) / 2.0
    ))
  }

  private def velocityCalc(obj: PointMass, timeStep: Double): PointMass = {
    obj.copy(velocity = Velocity(
      obj.velocity.x + obj.accel.x * timeStep,
      obj.velocity.y + obj.accel.y * timeStep
    ))
  }
}
