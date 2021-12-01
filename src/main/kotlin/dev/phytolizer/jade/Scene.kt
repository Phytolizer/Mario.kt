package dev.phytolizer.jade

abstract class Scene {
    protected abstract var camera: Camera
    open fun init() {}
    abstract fun update(dt: Float)
}