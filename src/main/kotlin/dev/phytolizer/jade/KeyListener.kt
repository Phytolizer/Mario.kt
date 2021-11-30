package dev.phytolizer.jade

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE

object KeyListener {
    private val keyPressed = MutableList(350) { false }

    fun keyCallback(
        window: Long,
        key: Int,
        scancode: Int,
        action: Int,
        mods: Int
    ) {
        when (action) {
            GLFW_PRESS -> keyPressed[key] = true
            GLFW_RELEASE -> keyPressed[key] = false
        }
    }

    fun isKeyPressed(keyCode: Int): Boolean {
        return keyPressed[keyCode]
    }
}