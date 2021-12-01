package dev.phytolizer.jade

import dev.phytolizer.jade.renderer.Shader
import org.joml.Vector2f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.glDrawElements
import org.lwjgl.opengl.GL15.glBufferData
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays

class LevelEditorScene : Scene() {
    override var camera = Camera(Vector2f())
    private val vertexArray = listOf(
        100.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
        0.0f, 100.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
        100.0f, 100.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
    )
    private val elementArray = listOf(
        2, 1, 0,
        0, 1, 3,
    )
    private var vaoId = 0
    private var vboId = 0
    private var eboId = 0
    private var defaultShader = Shader()

    override fun init() {
        camera = Camera(Vector2f())
        defaultShader = Shader("assets/shaders/default.glsl")
        defaultShader.compile()

        // =========================
        // Generate VAO, VBO, EBO...
        // =========================
        vaoId = glGenVertexArrays()
        glBindVertexArray(vaoId)

        // Create a float buffer of vertices
        val vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.size)
        vertexBuffer.put(vertexArray.toFloatArray()).flip()

        // Create VBO
        vboId = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)

        // Create index buffer
        val elementBuffer = BufferUtils.createIntBuffer(elementArray.size)
        elementBuffer.put(elementArray.toIntArray()).flip()

        eboId = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW)

        // Add vertex attribute pointers
        val positionsSize = 3
        val colorSize = 4
        val vertexSizeBytes = (positionsSize + colorSize) * Float.SIZE_BYTES
        glVertexAttribPointer(
            0,
            positionsSize,
            GL_FLOAT,
            false,
            vertexSizeBytes,
            0
        )
        glEnableVertexAttribArray(0)

        glVertexAttribPointer(
            1,
            colorSize,
            GL_FLOAT,
            false,
            vertexSizeBytes,
            (positionsSize * Float.SIZE_BYTES).toLong()
        )
        glEnableVertexAttribArray(1)
    }

    override fun update(dt: Float) {
        camera.position.x -= dt * 50.0f
        // Bind shader program
        defaultShader.use()
        defaultShader.uploadMat4f("uProjection", camera.projectionMatrix)
        defaultShader.uploadMat4f("uView", camera.viewMatrix)
        // Bind VAO
        glBindVertexArray(vaoId)

        // Enable vertex attrib pointers
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        glDrawElements(GL_TRIANGLES, elementArray.size, GL_UNSIGNED_INT, 0)

        // Unbind everything
        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindVertexArray(0)
        defaultShader.detach()
    }
}