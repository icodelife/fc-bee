package com.icodelife.fcbee.scenes

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.view.MotionEvent
import com.icodelife.fcbee.utils.ResourceManager
import com.icodelife.fcbee.utils.TextPainter

/**
 * Victory scene - shown when player wins
 */
class VictoryScene(
    private val screenWidth: Float,
    private val screenHeight: Float,
    private val onRestart: () -> Unit
) : Scene() {

    private lateinit var background: Bitmap
    private var backgroundY: Float = 0f
    private val backgroundScrollSpeed: Float = 100f

    private val titlePaint = TextPainter(80f, Color.WHITE)
    private val scorePaint = TextPainter(40f, Color.WHITE)
    private val restartPaint = TextPainter(50f, Color.WHITE)
    
    private val restartButton = RectF()
    private var isRestartPressed = false
    private var finalScore: Int = 0

    fun setScore(score: Int) {
        finalScore = score
    }

    override fun init() {
        background = ResourceManager.getBitmap("background")!!
        backgroundY = 0f
    }

    override fun update(deltaTime: Float) {
        // Scroll background
        backgroundY += backgroundScrollSpeed * deltaTime
        if (backgroundY >= screenHeight) {
            backgroundY = 0f
        }
    }

    override fun draw(canvas: Canvas) {
        // Draw scrolling background
        canvas.drawBitmap(background, 0f, backgroundY - screenHeight, null)
        canvas.drawBitmap(background, 0f, backgroundY, null)

        // Draw victory text
        titlePaint.drawCenteredText(canvas, "YOU WIN", screenWidth / 2, screenHeight / 2 - 150)
        scorePaint.drawCenteredText(canvas, "SCORE: $finalScore", screenWidth / 2, screenHeight / 2 - 50)

        // Draw restart button
        val restartText = "TAP TO RESTART"
        val restartY = screenHeight / 2 + 150
        restartPaint.drawCenteredText(canvas, restartText, screenWidth / 2, restartY)

        // Update button bounds
        val textWidth = restartPaint.paint.measureText(restartText)
        restartButton.set(
            screenWidth / 2 - textWidth / 2 - 20,
            restartY - 60,
            screenWidth / 2 + textWidth / 2 + 20,
            restartY + 20
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (restartButton.contains(event.x, event.y)) {
                    isRestartPressed = true
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isRestartPressed && restartButton.contains(event.x, event.y)) {
                    onRestart()
                }
                isRestartPressed = false
                return true
            }
        }
        return false
    }

    override fun cleanup() {
    }
}

/**
 * Defeat scene - shown when player loses
 */
class DefeatScene(
    private val screenWidth: Float,
    private val screenHeight: Float,
    private val onRestart: () -> Unit
) : Scene() {

    private lateinit var background: Bitmap
    private var backgroundY: Float = 0f
    private val backgroundScrollSpeed: Float = 100f

    private val titlePaint = TextPainter(80f, Color.WHITE)
    private val subtitlePaint = TextPainter(40f, Color.WHITE)
    private val restartPaint = TextPainter(50f, Color.WHITE)
    
    private val restartButton = RectF()
    private var isRestartPressed = false

    override fun init() {
        background = ResourceManager.getBitmap("background")!!
        backgroundY = 0f
    }

    override fun update(deltaTime: Float) {
        // Scroll background
        backgroundY += backgroundScrollSpeed * deltaTime
        if (backgroundY >= screenHeight) {
            backgroundY = 0f
        }
    }

    override fun draw(canvas: Canvas) {
        // Draw scrolling background
        canvas.drawBitmap(background, 0f, backgroundY - screenHeight, null)
        canvas.drawBitmap(background, 0f, backgroundY, null)

        // Draw defeat text
        titlePaint.drawCenteredText(canvas, "YOU DIED", screenWidth / 2, screenHeight / 2 - 150)
        subtitlePaint.drawCenteredText(canvas, "LOSE ALL SOULS", screenWidth / 2, screenHeight / 2 - 50)

        // Draw restart button
        val restartText = "TAP TO RESTART"
        val restartY = screenHeight / 2 + 150
        restartPaint.drawCenteredText(canvas, restartText, screenWidth / 2, restartY)

        // Update button bounds
        val textWidth = restartPaint.paint.measureText(restartText)
        restartButton.set(
            screenWidth / 2 - textWidth / 2 - 20,
            restartY - 60,
            screenWidth / 2 + textWidth / 2 + 20,
            restartY + 20
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (restartButton.contains(event.x, event.y)) {
                    isRestartPressed = true
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isRestartPressed && restartButton.contains(event.x, event.y)) {
                    onRestart()
                }
                isRestartPressed = false
                return true
            }
        }
        return false
    }

    override fun cleanup() {
    }
}
