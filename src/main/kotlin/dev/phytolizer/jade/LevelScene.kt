package dev.phytolizer.jade

import org.joml.Vector2f

class LevelScene : Scene() {
    override var camera = Camera(Vector2f())

    init {
        println("Inside LevelScene")
        Window.r = 1.0f
        Window.g = 1.0f
        Window.b = 1.0f
    }

    override fun update(dt: Float) {
    }
}