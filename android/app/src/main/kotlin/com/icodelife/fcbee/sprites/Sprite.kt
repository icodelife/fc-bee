package com.icodelife.fcbee.sprites

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF

/**
 * Base sprite class for all game objects
 */
abstract class Sprite(
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
    var bitmap: Bitmap?
) {
    var velocityX: Float = 0f
    var velocityY: Float = 0f
    var isActive: Boolean = true
    var isVisible: Boolean = true

    open fun update(deltaTime: Float) {
        x += velocityX * deltaTime
        y += velocityY * deltaTime
    }

    open fun draw(canvas: Canvas) {
        if (isVisible && bitmap != null) {
            canvas.drawBitmap(bitmap!!, x - width / 2, y - height / 2, null)
        }
    }

    fun getBounds(): RectF {
        return RectF(
            x - width / 2,
            y - height / 2,
            x + width / 2,
            y + height / 2
        )
    }

    fun collidesWith(other: Sprite): Boolean {
        return RectF.intersects(getBounds(), other.getBounds())
    }

    open fun destroy() {
        isActive = false
        isVisible = false
    }
}
