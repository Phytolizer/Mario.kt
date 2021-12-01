package dev.phytolizer.jade

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.glDrawElements
import org.lwjgl.opengl.GL15.glBufferData
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays

class LevelEditorScene : Scene() {
    private val vertexShaderSource =
        """#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;

out vec4 fColor;

void main()
{
   fColor = aColor;
   gl_Position = vec4(aPos, 1.0);
}
"""
    private val fragmentShaderSource =
        """#version 330 core

in vec4 fColor;

out vec4 color;

void main()
{
    color = fColor;
}
"""
    private var vertexId = 0
    private var fragmentId = 0
    private var shaderProgram = 0
    private val vertexArray = listOf(
        0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
        -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
        -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
    )
    private val elementArray = listOf(
        2, 1, 0,
        0, 1, 3,
    )
    private var vaoId = 0
    private var vboId = 0
    private var eboId = 0

    override fun init() {
        // ====================
        // Compile/link shaders
        // ====================

        // Load and compile vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER)
        // Pass shader source code to GPU
        glShaderSource(vertexId, vertexShaderSource)
        glCompileShader(vertexId)
        var success = glGetShaderi(vertexId, GL_COMPILE_STATUS)
        if (success == GL_FALSE) {
            val len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH)
            println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed.")
            println(glGetShaderInfoLog(vertexId, len))
            assert(false)
        }

        // Load and compile fragment shader
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER)
        // Pass shader source code to GPU
        glShaderSource(fragmentId, fragmentShaderSource)
        glCompileShader(fragmentId)
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS)
        if (success == GL_FALSE) {
            val len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH)
            println("ERROR: 'defaultShader.glsl'\n\tFragment shader compilation failed.")
            println(glGetShaderInfoLog(fragmentId, len))
            assert(false)
        }

        // Link shaders, check for errors
        shaderProgram = glCreateProgram()
        glAttachShader(shaderProgram, vertexId)
        glAttachShader(shaderProgram, fragmentId)
        glLinkProgram(shaderProgram)
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS)
        if (success == GL_FALSE) {
            val len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH)
            println("ERROR: 'defaultShader.glsl'\n\tShader program linking failed.")
            println(glGetProgramInfoLog(shaderProgram, len))
            assert(false)
        }

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
        // Bind shader program
        glUseProgram(shaderProgram)
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
        glUseProgram(0)
    }
}