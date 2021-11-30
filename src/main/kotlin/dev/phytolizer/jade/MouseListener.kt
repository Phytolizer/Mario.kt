package dev.phytolizer.jade

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE

object MouseListener {
    private var scrollX_ = 0.0
    private var scrollY_ = 0.0
    private var xPos = 0.0
    private var yPos = 0.0
    private var lastX = 0.0
    private var lastY = 0.0
    private val mouseButtonPressed = mutableListOf(false, false, false)
    private var isDragging_ = false

    fun mousePosCallback(window: Long, xpos: Double, ypos: Double) {
        lastX = xPos
        lastY = yPos
        xPos = xpos
        yPos = ypos
        isDragging_ = mouseButtonPressed.any { it }
    }

    fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        when (action) {
            GLFW_PRESS -> {
                if (button < mouseButtonPressed.size) {
                    mouseButtonPressed[button] = true
                }
            }
            GLFW_RELEASE -> {
                if (button < mouseButtonPressed.size) {
                    mouseButtonPressed[button] = false
                }
                isDragging_ = false
            }
        }
    }

    fun mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
        scrollX_ = xOffset
        scrollY_ = yOffset
    }

    fun endFrame() {
        scrollX_ = 0.0
        scrollY_ = 0.0
        lastX = xPos
        lastY = yPos
    }

    val x: Float
        get() = xPos.toFloat()
    val y: Float
        get() = yPos.toFloat()
    val dx: Float
        get() = (lastX - xPos).toFloat()
    val dy: Float
        get() = (lastY - yPos).toFloat()
    val scrollX: Float
        get() = scrollX_.toFloat()
    val scrollY: Float
        get() = scrollY_.toFloat()
    val isDragging: Boolean
        get() = isDragging_

    fun mouseButtonDown(button: Int): Boolean {
        return if (button < mouseButtonPressed.size) {
            mouseButtonPressed[button]
        } else {
            false
        }
    }
}

