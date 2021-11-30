package dev.phytolizer.jade

import org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE

class LevelEditorScene : Scene() {
    private var changingScene = false
    private var timeToChangeScene = 2.0f

    init {
        println("Inside LevelEditorScene")
    }

    override fun update(dt: Float) {
        println("${1.0f / dt} FPS")

        if (!changingScene && KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            changingScene = true
        }

        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt
            Window.r -= dt * 5.0f
            Window.g -= dt * 5.0f
            Window.b -= dt * 5.0f
        } else if (changingScene) {
            Window.changeScene(1)
        }
    }
}