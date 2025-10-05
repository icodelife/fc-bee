package com.icodelife.fcbee.sprites

import android.graphics.Bitmap
import android.graphics.Canvas

/**
 * Bullet base class
 */
abstract class Bullet(
    x: Float,
    y: Float,
    bitmap: Bitmap,
    private val speed: Float,
    private val lifeTime: Float
) : Sprite(x, y, bitmap.width.toFloat(), bitmap.height.toFloat(), bitmap) {
    
    private var born: Float = 0f
    private var direction: Float = 0f
    private var xSpeed: Float = 0f
    private var ySpeed: Float = 0f

    fun fire(shooterX: Float, shooterY: Float, targetX: Float, targetY: Float) {
        x = shooterX
        y = shooterY
        
        direction = Math.atan2((targetX - x).toDouble(), (targetY - y).toDouble()).toFloat()
        
        if (targetY >= y) {
            xSpeed = speed * Math.sin(direction.toDouble()).toFloat()
            ySpeed = speed * Math.cos(direction.toDouble()).toFloat()
        } else {
            xSpeed = -speed * Math.sin(direction.toDouble()).toFloat()
            ySpeed = -speed * Math.cos(direction.toDouble()).toFloat()
        }
        
        born = 0f
        isActive = true
        isVisible = true
    }

    override fun update(deltaTime: Float) {
        x += xSpeed * deltaTime
        y += ySpeed * deltaTime
        
        born += deltaTime
        
        if (born > lifeTime) {
            destroy()
        }
    }
}

/**
 * Fighter bullet
 */
class FighterBullet(x: Float, y: Float, bitmap: Bitmap) : 
    Bullet(x, y, bitmap, 600f, 1.8f)

/**
 * Bee (Galaxing) bullet
 */
class BeeBullet(x: Float, y: Float, bitmap: Bitmap) : 
    Bullet(x, y, bitmap, 300f, 5.0f)
