package dev.phytolizer.util

object Time {
    val timeStarted = System.nanoTime()

    val time: Float
        get() = (System.nanoTime() - timeStarted).toFloat() / 1e9f
}