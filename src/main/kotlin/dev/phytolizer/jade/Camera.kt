package dev.phytolizer.jade

import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f

class Camera(val position: Vector2f) {
    val projectionMatrix = Matrix4f()
    private var viewMatrixVar = Matrix4f()

    init {
        adjustProjection()
    }

    private fun adjustProjection() {
        projectionMatrix.identity()
        projectionMatrix.ortho(
            0.0f,
            32.0f * 40.0f,
            0.0f,
            32.0f * 21.0f,
            0.0f,
            100.0f
        )
    }

    val viewMatrix: Matrix4f
        get() {
            val cameraFront = Vector3f(0.0f, 0.0f, -1.0f)
            val cameraUp = Vector3f(0.0f, 1.0f, 0.0f)
            this.viewMatrixVar.setLookAt(
                Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f),
                cameraUp,
            )
            return this.viewMatrixVar
        }
}