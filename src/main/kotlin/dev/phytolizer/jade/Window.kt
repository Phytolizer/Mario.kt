package dev.phytolizer.jade

import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL

object Window {
    private var glfwWindow = NULL
    private const val width = 1920
    private const val height = 1080
    private const val title = "Mario"

    fun run() {
        println("Hello LWJGL ${Version.getVersion()}!")

        init()
        loop()
    }

    private fun init() {
        // Set up an error callback
        GLFWErrorCallback.createPrint(System.err).set()

        // Initialize GLFW
        if (!glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        // Configure GLFW
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE)

        // Create the window
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL)
        if (glfwWindow == NULL) {
            throw IllegalStateException("Failed to create the GLFW window")
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow)
        // Enable vsync
        glfwSwapInterval(1)

        // Make the window visible
        glfwShowWindow(glfwWindow)

        GL.createCapabilities()
    }

    private fun loop() {
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents()

            glClearColor(1.0f, 0.0f, 0.0f, 1.0f)
            glClear(GL_COLOR_BUFFER_BIT)

            glfwSwapBuffers(glfwWindow)
        }
    }
}