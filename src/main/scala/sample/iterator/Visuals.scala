package sample.iterator

import java.awt._
import java.awt.event.{ActionEvent, KeyEvent}
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage

import javax.swing._
import sample.iterator.PointMass.Position

import scala.collection.mutable.ListBuffer
import scala.swing.Dimension

case class Visuals(var objects: Seq[PointMass], var centerOn: Int) {

  var offsetCoords: Position = objects(centerOn).position
  var isFollowing: Boolean = false

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

  def addComponent(component: Component, anchor: Int = GridBagConstraints.PAGE_START, gridx: Int, gridy: Int, gridheight: Int = 1, gridwidth: Int = 1, weightx: Double = 1.0, weighty: Double = 1.0, fill: Int = GridBagConstraints.NONE): Unit = {
    constraints.anchor = anchor
    constraints.gridx = gridx
    constraints.gridy = gridy
    constraints.gridheight = gridheight
    constraints.gridwidth = gridwidth
    constraints.weightx = weightx
    constraints.weighty = weighty
    constraints.fill = fill
    frame.add(component, constraints)
  }

  val frame: JFrame = new JFrame
  frame.setLayout(new GridBagLayout)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  frame.setSize(1650, 1000)
  val constraints: GridBagConstraints = new GridBagConstraints()

  val buttonNext: JButton = new JButton("Next planet") {
    setPreferredSize(new Dimension(150, 50))
    new AbstractAction() {
      addActionListener(this)

      override def actionPerformed(e: ActionEvent): Unit = {
        centerOn = (centerOn + 1) % objects.length
        if (!isFollowing) isFollowing = true
      }
    }
  }

  val buttonPrevious: JButton = new JButton("Previous planet") {
    setPreferredSize(new Dimension(150, 50))
    new AbstractAction() {
      addActionListener(this)

      override def actionPerformed(e: ActionEvent): Unit = {
        centerOn = if (centerOn == 0) objects.length - 1
        else centerOn - 1
        if (!isFollowing) isFollowing = true
      }
    }
  }

  val panel: JPanel = new JPanel {
    override protected def paintComponent(g: Graphics): Unit = {
      super.paintComponent(g)
      objects.zipWithIndex.foreach { case (obj, i) =>
        g.drawImage(planetImages(i)._1, (obj.position.x + frame.getWidth / 2 - planetImages(i)._2 / 2 - offsetCoords.x).toInt, (-obj.position.y + frame.getHeight / 2 - planetImages(i)._2 / 2 + offsetCoords.y).toInt, null)
      }
      trail.foreach {
        _.zipWithIndex.foreach { case (pos, i) =>
          g.drawImage(planetImages(i)._3, (pos.x + frame.getWidth / 2 - 1 - offsetCoords.x).toInt, (-pos.y + frame.getHeight / 2 - 1 + offsetCoords.y).toInt, null)
        }
      }
      g.dispose()
    }

    val inputMap: InputMap = getInputMap
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up")
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down")
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left")
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right")

    val actionMap: ActionMap = getActionMap
    actionMap.put("up", new AbstractAction() {
      override def actionPerformed(e: ActionEvent): Unit = {
        if (isFollowing) isFollowing = false
        offsetCoords = offsetCoords.copy(y = offsetCoords.y + 10)
      }
    })
    actionMap.put("down", new AbstractAction() {
      override def actionPerformed(e: ActionEvent): Unit = {
        if (isFollowing) isFollowing = false
        offsetCoords = offsetCoords.copy(y = offsetCoords.y - 10)
      }
    })
    actionMap.put("left", new AbstractAction() {
      override def actionPerformed(e: ActionEvent): Unit = {
        if (isFollowing) isFollowing = false
        offsetCoords = offsetCoords.copy(x = offsetCoords.x - 10)
      }
    })
    actionMap.put("right", new AbstractAction() {
      override def actionPerformed(e: ActionEvent): Unit = {
        if (isFollowing) isFollowing = false
        offsetCoords = offsetCoords.copy(x = offsetCoords.x + 10)
      }
    })
  }
  addComponent(Box.createGlue(), gridx = 0, gridy = 0)
  addComponent(Box.createGlue(), gridx = 1, gridy = 0)
  addComponent(Box.createGlue(), gridx = 2, gridy = 0)
  addComponent(Box.createGlue(), gridx = 3, gridy = 0)
  addComponent(Box.createGlue(), gridx = 4, gridy = 0)
  addComponent(Box.createGlue(), gridx = 7, gridy = 0)
  addComponent(Box.createGlue(), gridx = 8, gridy = 0)
  addComponent(Box.createGlue(), gridx = 9, gridy = 0)
  addComponent(Box.createGlue(), gridx = 10, gridy = 0)
  addComponent(Box.createGlue(), gridx = 11, gridy = 0)
  addComponent(Box.createGlue(), gridx = 11, gridy = 19)
  addComponent(buttonPrevious, gridx = 5, gridy = 0)
  addComponent(buttonNext, gridx = 6, gridy = 0)
  addComponent(panel, GridBagConstraints.CENTER, gridx = 0, gridy = 0, gridheight = 20, gridwidth = 12, fill = GridBagConstraints.BOTH)
  frame.setLocationRelativeTo(panel)
  frame.setVisible(true)

  val timer = new Timer(10, (_: ActionEvent) => {
    trail.append(objects.map(_.position))
    if (trail.length > 200) trail.remove(0)
    if (isFollowing) offsetCoords = objects(centerOn).position
    frame.repaint()
  })

  timer.start()

}
