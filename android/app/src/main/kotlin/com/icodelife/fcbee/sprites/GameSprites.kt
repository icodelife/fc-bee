package com.icodelife.fcbee.sprites

import android.graphics.Bitmap
import android.graphics.Canvas

/**
 * Fighter (player) sprite
 */
class Fighter(
    x: Float,
    y: Float,
    bitmap: Bitmap
) : Sprite(x, y, bitmap.width.toFloat(), bitmap.height.toFloat(), bitmap) {
    
    var canShoot: Boolean = true
    private var shootCooldown: Float = 0f
    private val shootDelay: Float = 0.3f

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        
        if (!canShoot) {
            shootCooldown += deltaTime
            if (shootCooldown >= shootDelay) {
                canShoot = true
                shootCooldown = 0f
            }
        }
    }

    fun shoot(): Boolean {
        if (canShoot) {
            canShoot = false
            return true
        }
        return false
    }
}

/**
 * Bee (Galaxing) sprite with animation
 */
class Bee(
    x: Float,
    y: Float,
    private val frames: List<Bitmap>,
    val type: Int // 0, 1, 2 for different bee types
) : Sprite(x, y, frames[0].width.toFloat(), frames[0].height.toFloat(), frames[0]) {
    
    private var animTime: Float = 0f
    private var frameIndex: Int = 0
    private val frameRate: Float = 0.5f // frames per second

    var moveDirection: Float = 1f // 1 for right, -1 for left
    var moveSpeed: Float = 100f
    var moveRange: Float = 250f
    var startX: Float = x

    override fun update(deltaTime: Float) {
        // Update animation
        animTime += deltaTime
        if (animTime >= frameRate) {
            animTime = 0f
            frameIndex = (frameIndex + 1) % frames.size
            bitmap = frames[frameIndex]
        }

        // Update movement (oscillate left and right)
        x += moveDirection * moveSpeed * deltaTime
        
        if (Math.abs(x - startX) > moveRange) {
            moveDirection *= -1f
        }

        super.update(deltaTime)
    }

    fun canShoot(): Boolean {
        return Math.random() < 0.001 // Random chance to shoot
    }
}

/**
 * Explosion sprite with animation
 */
class Explosion(
    x: Float,
    y: Float,
    private val frames: List<Bitmap>
) : Sprite(x, y, frames[0].width.toFloat(), frames[0].height.toFloat(), frames[0]) {
    
    private var animTime: Float = 0f
    private var frameIndex: Int = 0
    private val frameRate: Float = 0.033f // 30 fps
    private var isPlaying: Boolean = false

    fun show(posX: Float, posY: Float) {
        x = posX
        y = posY
        isActive = true
        isVisible = true
        isPlaying = true
        frameIndex = 0
        animTime = 0f
        bitmap = frames[0]
    }

    override fun update(deltaTime: Float) {
        if (!isPlaying) return

        animTime += deltaTime
        if (animTime >= frameRate) {
            animTime = 0f
            frameIndex++
            
            if (frameIndex >= frames.size) {
                isPlaying = false
                destroy()
            } else {
                bitmap = frames[frameIndex]
            }
        }
    }
}
