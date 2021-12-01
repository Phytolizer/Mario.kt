package dev.phytolizer.jade.renderer

import org.lwjgl.opengl.GL20.*
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class Shader() {
    private var filePath = ""
    private var shaderProgramId = 0
    private var vertexSource = ""
    private var fragmentSource = ""

    constructor(filePath: String) : this() {
        this.filePath = filePath
        try {
            val source = String(Files.readAllBytes(Paths.get(filePath)))
            val matches = Regex(
                """^#type\s+(vertex|fragment)""",
                RegexOption.MULTILINE
            ).findAll(source).toList()
            for ((i, match) in matches.withIndex()) {
                val slice = if (i == matches.size - 1) {
                    (match.range.last + 1) until source.length
                } else {
                    (match.range.last + 1) until matches[i + 1].range.first
                }
                val substr = source.substring(slice)
                when (match.groupValues[1]) {
                    "vertex" -> vertexSource = substr
                    "fragment" -> fragmentSource = substr
                    else -> throw IOException("Unexpected token '${match.groupValues[1]}' in '$filePath'")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            assert(false) { "Error: Could not open file for shader: '$filePath'" }
        }

        println("============ VERTEX ============")
        println(vertexSource)
        println("=========== FRAGMENT ===========")
        println(fragmentSource)
    }

    fun compile() {
        // Load and compile vertex shader
        val vertexId = glCreateShader(GL_VERTEX_SHADER)
        // Pass shader source code to GPU
        glShaderSource(vertexId, vertexSource)
        glCompileShader(vertexId)
        var success = glGetShaderi(vertexId, GL_COMPILE_STATUS)
        if (success == GL_FALSE) {
            val len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH)
            println("ERROR: '$filePath'\n\tVertex shader compilation failed.")
            println(glGetShaderInfoLog(vertexId, len))
            assert(false)
        }

        // Load and compile fragment shader
        val fragmentId = glCreateShader(GL_FRAGMENT_SHADER)
        // Pass shader source code to GPU
        glShaderSource(fragmentId, fragmentSource)
        glCompileShader(fragmentId)
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS)
        if (success == GL_FALSE) {
            val len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH)
            println("ERROR: '$filePath'\n\tFragment shader compilation failed.")
            println(glGetShaderInfoLog(fragmentId, len))
            assert(false)
        }

        // Link shaders, check for errors
        shaderProgramId = glCreateProgram()
        glAttachShader(shaderProgramId, vertexId)
        glAttachShader(shaderProgramId, fragmentId)
        glLinkProgram(shaderProgramId)
        success = glGetProgrami(shaderProgramId, GL_LINK_STATUS)
        if (success == GL_FALSE) {
            val len = glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH)
            println("ERROR: '$filePath'\n\tShader program linking failed.")
            println(glGetProgramInfoLog(shaderProgramId, len))
            assert(false)
        }

    }

    fun use() {
        glUseProgram(shaderProgramId)
    }

    fun detach() {
        glUseProgram(0)
    }
}