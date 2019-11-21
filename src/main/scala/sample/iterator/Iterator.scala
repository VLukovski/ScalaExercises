package sample.iterator

import sample.iterator.PointMass.{Force, Position, Velocity}

object Iterator {

  @scala.annotation.tailrec
  def iterate(objects: Seq[PointMass], timeStep: Double, times: Int = 1, visuals: Visuals): Seq[PointMass] = {
    if (times > 0) {
      visuals.objects = objects
      iterate(
        objects.map { obj =>
          velocityCalc(
            positionCalc(
              forceCalc(
                objects.filterNot(_ == obj),
                obj.copy(force = Force(0, 0))
              ),
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

  @scala.annotation.tailrec
  private def forceCalc(objList: Seq[PointMass], obj: PointMass, gravConst: Double = 1.0): PointMass = {
    if (objList.nonEmpty) {
      val distance = Math.max(Math.sqrt(Math.pow(objList.head.position.x - obj.position.x, 2) + Math.pow(objList.head.position.y - obj.position.y, 2)), 2)
      forceCalc(
        objList.tail,
        obj.copy(force = Force(
          obj.force.x + (-gravConst * objList.head.mass * (-objList.head.position.x + obj.position.x)) / Math.pow(distance, 3),
          obj.force.y + (-gravConst * objList.head.mass * (-objList.head.position.y + obj.position.y)) / Math.pow(distance, 3)
        )),
        gravConst
      )
    } else obj
  }

  private def positionCalc(obj: PointMass, timeStep: Double): PointMass = {
    obj.copy(position = Position(
      obj.position.x + obj.velocity.x * timeStep + obj.force.x * Math.pow(timeStep, 2) / 2.0,
      obj.position.y + obj.velocity.y * timeStep + obj.force.y * Math.pow(timeStep, 2) / 2.0
    ))
  }

  private def velocityCalc(obj: PointMass, timeStep: Double): PointMass = {
    obj.copy(velocity = Velocity(
      obj.velocity.x + obj.force.x * timeStep,
      obj.velocity.y + obj.force.y * timeStep
    ))
  }
}
