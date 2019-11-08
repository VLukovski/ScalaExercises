package sample.iterator

import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.awt.{Color, Dimension, Graphics, Graphics2D}

import javax.swing.{JFrame, JPanel}
import sample.iterator.PointMass.{Force, Position, Velocity}

object Iterator extends App {

  val objects = Seq(
    PointMass(
      mass = 100000,
      position = Position(0, 0),
      velocity = Velocity(0.11, -0.11),
      force = Force(0, 0)
    ),
    PointMass(
      mass = 1000,
      position = Position(-300, -300),
      velocity = Velocity(-10.5, 11),
      force = Force(0, 0)
    ),
    PointMass(
      mass = 10,
      position = Position(-315, -315),
      velocity = Velocity(-15.5, 16),
      force = Force(0, 0)
    ),
    PointMass(
      mass = 70,
      position = Position(200, -200),
      velocity = Velocity(-12, -11),
      force = Force(0, 0)
    ),
    PointMass(
      mass = 50,
      position = Position(70, 70),
      velocity = Velocity(-26, 25),
      force = Force(0, 0)
    )
  )

  val planetImages: Seq[(BufferedImage, Double)] = objects.map { obj =>
    val width: Double = Math.pow(obj.mass, 1.0 / 3) + 10

    val image: BufferedImage = {
      val canvas = new BufferedImage(width.toInt, width.toInt, BufferedImage.TYPE_INT_ARGB)
      val g = canvas.createGraphics()
      g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
      g.setColor(Color.BLACK)
      g.fill(new Ellipse2D.Double(0, 0, width, width))
      g.dispose()
      canvas
    }

    (image, width)
  }

  val frame = new JFrame()
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  frame.setSize(new Dimension(1000, 1000))
  frame.setLocationRelativeTo(null)
  frame.setVisible(true)

  iterate(objects, 0.0001, 100000000)

  @scala.annotation.tailrec
  def iterate(objects: Seq[PointMass], timeStep: Double, times: Int = 1): Seq[PointMass] = {
    if (times > 0) {
      if (times % 5000 == 0) {
        frame.getContentPane.removeAll()
        frame.add(planetarium(objects))
        frame.revalidate()
        frame.repaint()
      }
      //println(objects)
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
        times - 1
      )
    } else objects
  }

  case class planetarium(objects: Seq[PointMass]) extends JPanel {
    override protected def paintComponent(g: Graphics): Unit = {
      super.paintComponent(g)
      objects.zipWithIndex.foreach { case (obj, i) =>
        g.drawImage(planetImages(i)._1, (obj.position.x + 500 - planetImages(i)._2 / 2).toInt, (obj.position.y + 500 - planetImages(i)._2 / 2).toInt, null)
      }
    }
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

  @scala.annotation.tailrec
  private def forceCalc(objList: Seq[PointMass], obj: PointMass, gravConst: Double = 1.0): PointMass = {
    if (objList.nonEmpty) {
      val distance = Math.max(Math.sqrt(Math.pow(objList.head.position.x - obj.position.x, 2) + Math.pow(objList.head.position.y - obj.position.y, 2)), 5)
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
}
