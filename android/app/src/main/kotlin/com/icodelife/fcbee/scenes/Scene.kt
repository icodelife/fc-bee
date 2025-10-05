package com.icodelife.fcbee.scenes

import android.graphics.Canvas
import android.view.MotionEvent

/**
 * Base scene class
 */
abstract class Scene {
    var isActive: Boolean = false

    abstract fun init()
    abstract fun update(deltaTime: Float)
    abstract fun draw(canvas: Canvas)
    abstract fun onTouchEvent(event: MotionEvent): Boolean
    abstract fun cleanup()
}

/**
 * Scene manager to handle scene transitions
 */
class SceneManager {
    private var currentScene: Scene? = null
    private var nextScene: Scene? = null

    fun setScene(scene: Scene) {
        nextScene = scene
    }

    fun update(deltaTime: Float) {
        // Handle scene transitions
        nextScene?.let {
            currentScene?.cleanup()
            currentScene = it
            currentScene?.init()
            currentScene?.isActive = true
            nextScene = null
        }

        currentScene?.update(deltaTime)
    }

    fun draw(canvas: Canvas) {
        currentScene?.draw(canvas)
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        return currentScene?.onTouchEvent(event) ?: false
    }

    fun getCurrentScene(): Scene? {
        return currentScene
    }
}
