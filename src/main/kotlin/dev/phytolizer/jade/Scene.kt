package dev.phytolizer.jade

abstract class Scene {
    open fun init() {}
    abstract fun update(dt: Float)
}