package dev.phytolizer.jade

import dev.phytolizer.util.Time
import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
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
    var r = 1.0f
    var g = 1.0f
    var b = 1.0f
    private var a = 1.0f

    private var currentScene: Scene? = null

    private fun changeScene(newScene: Int) {
        when (newScene) {
            0 -> currentScene = LevelEditorScene()
            1 -> currentScene = LevelScene()
            else -> assert(false) { "Unknown scene $newScene" }
        }
        currentScene!!.init()
    }

    fun run() {
        println("Hello LWJGL ${Version.getVersion()}!")

        init()
        loop()

        glfwFreeCallbacks(glfwWindow)
        glfwDestroyWindow(glfwWindow)
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
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

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback)
        glfwSetMouseButtonCallback(
            glfwWindow,
            MouseListener::mouseButtonCallback
        )
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback)
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback)

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow)
        // Enable vsync
        glfwSwapInterval(1)

        // Make the window visible
        glfwShowWindow(glfwWindow)

        GL.createCapabilities()

        changeScene(0)
    }

    private fun loop() {
        var beginTime = Time.time
        var endTime: Float
        var dt = -1.0f

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents()

            glClearColor(r, g, b, a)
            glClear(GL_COLOR_BUFFER_BIT)

            if (dt >= 0) {
                currentScene!!.update(dt)
            }

            glfwSwapBuffers(glfwWindow)

            endTime = Time.time
            dt = endTime - beginTime
            beginTime = endTime
        }
    }
}