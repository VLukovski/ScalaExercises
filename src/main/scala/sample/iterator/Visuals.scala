package sample.iterator

import java.awt._
import java.awt.event.{ActionEvent, KeyEvent, KeyListener}
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage

import javax.swing._
import sample.iterator.PointMass.Position

import scala.collection.mutable.ListBuffer

case class Visuals(var objects: Seq[PointMass], var centerOn: Int) {

  var offsetCoords: Position = objects(centerOn).position
  var isFollowing: Boolean = true
  var showGrid: Boolean = false
  var frameTime: Double = 10
  val movementKeys: ListBuffer[Boolean] = ListBuffer(false, false, false, false)
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

  val frame: JFrame = new JFrame with KeyListener {
    addKeyListener(this)

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
        g.drawImage(planetImages(i)._1, (obj.position.x + widthHalved - planetImages(i)._2 / 2 - offsetCoords.x).toInt, (-obj.position.y + heightHalved - planetImages(i)._2 / 2 + offsetCoords.y).toInt, null)
      }
      trail.foreach {
        _.zipWithIndex.foreach { case (pos, i) =>
          g.drawImage(planetImages(i)._3, (pos.x + widthHalved - 1 - offsetCoords.x).toInt, (-pos.y + heightHalved - 1 + offsetCoords.y).toInt, null)
        }
      }
      if (showGrid) {
        val g2d = g.create.asInstanceOf[Graphics2D]
        val dashed: Stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, Array[Float](10), 0)
        g2d.setStroke(dashed)
        for (i <- 0 to 10; j <- 0 to 6) {
          g2d.drawLine((-gridScale - offsetCoords.x % gridScale).toInt, (gridScale * (j - 1) + offsetCoords.y % gridScale).toInt, (gridScale * 20 - offsetCoords.x % gridScale).toInt, (gridScale * (j - 1) + offsetCoords.y % gridScale).toInt)
          g2d.drawLine((gridScale * (i - 1) - offsetCoords.x % gridScale).toInt, (-gridScale + offsetCoords.y % gridScale).toInt, (gridScale * (i - 1) - offsetCoords.x % gridScale).toInt, (gridScale * 12 + offsetCoords.y % gridScale).toInt)
        }
        g2d.dispose()
      }
      g.dispose()
      while (System.nanoTime() - time < 10000000) {}
      frameTime = BigDecimal((System.nanoTime() - time) / 1000000.0).setScale(1, BigDecimal.RoundingMode.HALF_UP).toDouble
      if (frameTime > 10) frameTimer.setDelay(frameTime.toInt)
    }
  }

  def moveCamera(): Unit = {
    (movementKeys(0), movementKeys(1), movementKeys(2), movementKeys(3)) match {
      case (true, false, false, true) => offsetCoords = Position(offsetCoords.x - 7.07, offsetCoords.y + 7.07)
      case (true, false, true, false) => offsetCoords = Position(offsetCoords.x + 7.07, offsetCoords.y + 7.07)
      case (false, true, false, true) => offsetCoords = Position(offsetCoords.x - 7.07, offsetCoords.y - 7.07)
      case (false, true, true, false) => offsetCoords = Position(offsetCoords.x + 7.07, offsetCoords.y - 7.07)
      case (true, false, _, _) => offsetCoords = Position(offsetCoords.x, offsetCoords.y + 10)
      case (false, true, _, _) => offsetCoords = Position(offsetCoords.x, offsetCoords.y - 10)
      case (_, _, true, false) => offsetCoords = Position(offsetCoords.x + 10, offsetCoords.y)
      case (_, _, false, true) => offsetCoords = Position(offsetCoords.x - 10, offsetCoords.y)
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
    trail.append(objects.map(_.position))
    if (trail.length > 200) trail.remove(0)
    if (!isFollowing) moveCamera()
  })
  val frameTimer = new Timer(frameTime.toInt, (_: ActionEvent) => {
    if (isFollowing) offsetCoords = objects(centerOn).position
    fps.setText(frameTime.toString + "ms")
    frame.repaint()
  })

  staticTimer.start()
  frameTimer.start()

}
