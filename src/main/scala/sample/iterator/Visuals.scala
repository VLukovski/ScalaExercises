package sample.iterator

import java.awt._
import java.awt.event._
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage

import javax.swing._
import sample.iterator.PointMass.Vector2D

import scala.collection.mutable.ListBuffer

case class Visuals(var objects: Seq[PointMass], var centerOn: Int) {

  var offsetCoords: Vector2D = objects(centerOn).position
  var isFollowing: Boolean = true
  var showGrid: Boolean = false
  val movementKeys: ListBuffer[Boolean] = ListBuffer(false, false, false, false)
  val zoomKeys: ListBuffer[Boolean] = ListBuffer(false, false)
  val trail: ListBuffer[Seq[Vector2D]] = ListBuffer()
  var zoom: Double = 1.0


  def planetConstantsGenerator: Seq[(BufferedImage, BufferedImage)] = objects.map { obj =>

    val image: BufferedImage = {
      val canvas = new BufferedImage((obj.width * zoom).ceil.toInt, (obj.width * zoom).ceil.toInt, BufferedImage.TYPE_INT_ARGB)
      val g = canvas.createGraphics()
      g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
      g.setColor(Color.BLACK)
      g.fill(new Ellipse2D.Double(0, 0, obj.width * zoom, obj.width * zoom))
      g.dispose()
      canvas
    }

    val trail: BufferedImage = {
      val canvas = new BufferedImage((obj.width * zoom).ceil.toInt, (obj.width * zoom).ceil.toInt, BufferedImage.TYPE_INT_ARGB)
      val g = canvas.createGraphics()
      g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
      g.setColor(Color.BLACK)
      g.fill(new Ellipse2D.Double(0, 0, 2, 2))
      g.dispose()
      canvas
    }

    (image, trail)
  }

  var planetConstants: Seq[(BufferedImage, BufferedImage)] = planetConstantsGenerator

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

  val frame: JFrame = new JFrame with KeyListener with MouseWheelListener {
    addKeyListener(this)
    addMouseWheelListener(this)

    override protected def keyTyped(e: KeyEvent): Unit = {}

    override protected def keyPressed(e: KeyEvent): Unit = {
      val key = e.getKeyCode
      if (key == KeyEvent.VK_UP) movementKeys(0) = true
      else if (key == KeyEvent.VK_DOWN) movementKeys(1) = true
      else if (key == KeyEvent.VK_RIGHT) movementKeys(2) = true
      else if (key == KeyEvent.VK_LEFT) movementKeys(3) = true
      if (isFollowing) isFollowing = false
    }

    override protected def keyReleased(e: KeyEvent): Unit = {
      val key = e.getKeyCode
      if (key == KeyEvent.VK_UP) movementKeys(0) = false
      else if (key == KeyEvent.VK_DOWN) movementKeys(1) = false
      else if (key == KeyEvent.VK_RIGHT) movementKeys(2) = false
      else if (key == KeyEvent.VK_LEFT) movementKeys(3) = false
    }

    override protected def mouseWheelMoved(e: MouseWheelEvent): Unit = {
      val scrollUnits = e.getWheelRotation
      zoom = BigDecimal(zoom * Math.pow(1.05, -scrollUnits)).setScale(10, BigDecimal.RoundingMode.HALF_UP).toDouble
      planetConstants = planetConstantsGenerator
    }
  }
  frame.setLayout(new GridBagLayout)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  frame.setSize(1650, 1000)
  val constraints: GridBagConstraints = new GridBagConstraints()

  val fps: JLabel = new JLabel()
  val buttonGrid: JButton = new JButton("Show Grid") {
    setPreferredSize(new Dimension(150, 50))
    new AbstractAction() {
      addActionListener(this)
      setFocusable(false)

      override def actionPerformed(e: ActionEvent): Unit = {
        showGrid = !showGrid
      }
    }
  }
  val buttonNext: JButton = new JButton("Next Planet") {
    setPreferredSize(new Dimension(150, 50))
    new AbstractAction() {
      addActionListener(this)
      setFocusable(false)

      override def actionPerformed(e: ActionEvent): Unit = {
        if (isFollowing) centerOn = (centerOn + 1) % objects.length
        else isFollowing = true
      }
    }
  }

  val buttonPrevious: JButton = new JButton("Previous Planet") {
    setPreferredSize(new Dimension(150, 50))
    new AbstractAction() {
      addActionListener(this)
      setFocusable(false)

      override def actionPerformed(e: ActionEvent): Unit = {
        if (isFollowing) {
          centerOn = if (centerOn == 0) objects.length - 1
          else centerOn - 1
        }
        else isFollowing = true
      }
    }
  }

  val panel: JPanel = new JPanel {
    val gridScale: Double = 200

    override protected def paintComponent(g: Graphics): Unit = {
      super.paintComponent(g)
      val time = System.nanoTime()

      val widthHalved: Double = getWidth / 2
      val heightHalved: Double = getHeight / 2
      objects.zipWithIndex.foreach { case (obj, i) =>
        g.drawImage(
          planetConstants(i)._1,
          ((obj.position.x - offsetCoords.x - obj.width / 2) * zoom + widthHalved).toInt,
          ((-obj.position.y + offsetCoords.y - obj.width / 2) * zoom + heightHalved).toInt,
          null
        )
      }
      trail.foreach {
        _.zipWithIndex.foreach { case (pos, i) =>
          g.drawImage(
            planetConstants(i)._2,
            ((pos.x - offsetCoords.x) * zoom + widthHalved - 1).toInt,
            ((-pos.y + offsetCoords.y) * zoom + heightHalved - 1).toInt,
            null
          )
        }
      }
      if (showGrid) {
        val g2d = g.create.asInstanceOf[Graphics2D]
        val dashed: Stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, Array[Float](10), 15)
        g2d.setStroke(dashed)
        for (i <- 0 to 10; j <- 0 to 6) {
          g2d.drawLine(
            (-gridScale - (offsetCoords.x * zoom % gridScale)).toInt,
            (gridScale * j + (offsetCoords.y * zoom % gridScale)).toInt,
            (gridScale * 11 - (offsetCoords.x * zoom % gridScale)).toInt,
            (gridScale * j + (offsetCoords.y * zoom % gridScale)).toInt
          )
          g2d.drawLine(
            (gridScale * i - (offsetCoords.x * zoom % gridScale)).toInt,
            (-gridScale + (offsetCoords.y * zoom % gridScale)).toInt,
            (gridScale * i - (offsetCoords.x * zoom % gridScale)).toInt,
            (gridScale * 7 + (offsetCoords.y * zoom % gridScale)).toInt
          )
        }
        g2d.dispose()
      }
      g.dispose()
      while (System.nanoTime() - time < 10000000) {}
      val frameTime = BigDecimal((System.nanoTime() - time) / 1000000.0).setScale(1, BigDecimal.RoundingMode.HALF_UP).toDouble
      fps.setText(frameTime.toString + "ms")
      frameTimer.setDelay(frameTime.toInt)
    }
  }

  def moveCamera(): Unit = {
    (movementKeys(0), movementKeys(1), movementKeys(2), movementKeys(3)) match {
      case (true, false, false, true) => offsetCoords = Vector2D(offsetCoords.x - 7.07, offsetCoords.y + 7.07)
      case (true, false, true, false) => offsetCoords = Vector2D(offsetCoords.x + 7.07, offsetCoords.y + 7.07)
      case (false, true, false, true) => offsetCoords = Vector2D(offsetCoords.x - 7.07, offsetCoords.y - 7.07)
      case (false, true, true, false) => offsetCoords = Vector2D(offsetCoords.x + 7.07, offsetCoords.y - 7.07)
      case (true, false, _, _) => offsetCoords = Vector2D(offsetCoords.x, offsetCoords.y + 10)
      case (false, true, _, _) => offsetCoords = Vector2D(offsetCoords.x, offsetCoords.y - 10)
      case (_, _, true, false) => offsetCoords = Vector2D(offsetCoords.x + 10, offsetCoords.y)
      case (_, _, false, true) => offsetCoords = Vector2D(offsetCoords.x - 10, offsetCoords.y)
      case (_, _, _, _) =>
    }
  }

  addComponent(Box.createGlue(), gridx = 0, gridy = 0)
  addComponent(Box.createGlue(), gridx = 1, gridy = 0)
  addComponent(Box.createGlue(), gridx = 2, gridy = 0)
  addComponent(Box.createGlue(), gridx = 3, gridy = 0)
  addComponent(Box.createGlue(), gridx = 7, gridy = 0)
  addComponent(Box.createGlue(), gridx = 8, gridy = 0)
  addComponent(Box.createGlue(), gridx = 9, gridy = 0)
  addComponent(Box.createGlue(), gridx = 10, gridy = 0)

  addComponent(fps, anchor = GridBagConstraints.NORTHWEST, gridx = 0, gridy = 0, weightx = 0, weighty = 0, gridwidth = 0, gridheight = 0)
  addComponent(buttonGrid, gridx = 5, gridy = 0)
  addComponent(buttonPrevious, gridx = 4, gridy = 0)
  addComponent(buttonNext, gridx = 6, gridy = 0)
  addComponent(panel, gridx = 0, gridy = 0, gridwidth = 11, fill = GridBagConstraints.BOTH)
  frame.setLocationRelativeTo(panel)
  frame.setVisible(true)

  val staticTimer = new Timer(10, (_: ActionEvent) => {
//    trail.append(objects.map(_.position))
//    if (trail.length > 200) trail.remove(0)
    if (!isFollowing) moveCamera()
  })
  val frameTimer = new Timer(10, (_: ActionEvent) => {
    if (isFollowing) offsetCoords = objects(centerOn).position
    frame.repaint()
  })

  staticTimer.start()
  frameTimer.start()

}
