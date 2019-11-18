package sample.iterator

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.awt.{BorderLayout, Color, Dimension, Graphics}

import javax.swing.{JFrame, JPanel, Timer}
import sample.iterator.PointMass.Position

import scala.collection.mutable.ListBuffer

case class Visuals(var objects: Seq[PointMass]) {

  val trail: ListBuffer[Seq[Position]] = ListBuffer()

  val planetImages: Seq[(BufferedImage, Double, BufferedImage)] = objects.map { obj =>
    val width: Double = Math.pow(obj.mass, 1.0 / 3) + 10

    val image: BufferedImage = {
      val canvas = new BufferedImage(width.ceil.toInt, width.ceil.toInt, BufferedImage.TYPE_INT_ARGB)
      val g = canvas.createGraphics()
      g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
      g.setColor(Color.BLACK)
      g.fill(new Ellipse2D.Double(0, 0, width, width))
      g.dispose()
      canvas
    }

    val trail: BufferedImage = {
      val canvas = new BufferedImage(width.ceil.toInt, width.ceil.toInt, BufferedImage.TYPE_INT_ARGB)
      val g = canvas.createGraphics()
      g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
      g.setColor(Color.BLACK)
      g.fill(new Ellipse2D.Double(0, 0, 2, 2))
      g.dispose()
      canvas
    }

    (image, width, trail)
  }

  val panel: JPanel = new JPanel {
    override protected def paintComponent(g: Graphics): Unit = {
      super.paintComponent(g)
      objects.zipWithIndex.foreach { case (obj, i) =>
        g.drawImage(planetImages(i)._1, (obj.position.x + 800 - planetImages(i)._2 / 2).toInt, (-obj.position.y + 500 - planetImages(i)._2 / 2).toInt, null)
      }
      trail.foreach {
        _.zipWithIndex.foreach { case (pos, i) =>
          g.drawImage(planetImages(i)._3, (pos.x + 800 - 1).toInt, (-pos.y + 500 - 1).toInt, null)
        }
      }
      g.dispose()
    }
  }

  val timer = new Timer(10, new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      trail.append(objects.map(_.position))
      if (trail.length > 200) trail.remove(0)
      frame.repaint()
    }
  })

  val frame = new JFrame()
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  frame.setSize(new Dimension(1600, 1000))
  frame.setLayout(new BorderLayout)
  frame.add(panel, BorderLayout.CENTER)
  panel.setBounds(0, 0, 1600, 1000)
  frame.setLocationRelativeTo(null)
  frame.setVisible(true)
  timer.start()

}
