package com.icodelife.fcbee.scenes

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.view.MotionEvent
import com.icodelife.fcbee.utils.ResourceManager
import com.icodelife.fcbee.utils.TextPainter

/**
 * Start scene - initial game screen
 */
class StartScene(
    private val screenWidth: Float,
    private val screenHeight: Float,
    private val onStartGame: () -> Unit
) : Scene() {

    private lateinit var background: Bitmap
    private var backgroundY: Float = 0f
    private val backgroundScrollSpeed: Float = 100f
    
    private val titlePaint = TextPainter(80f, Color.WHITE)
    private val startPaint = TextPainter(50f, Color.WHITE)
    private val startButton = RectF()
    private var isStartPressed = false

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

        // Draw title
        titlePaint.drawCenteredText(canvas, "FC BEE", screenWidth / 2, screenHeight / 2 - 100)

        // Draw start button
        val startText = "TAP TO START"
        val startY = screenHeight / 2 + 150
        startPaint.drawCenteredText(canvas, startText, screenWidth / 2, startY)

        // Update button bounds for touch detection
        val textWidth = startPaint.paint.measureText(startText)
        startButton.set(
            screenWidth / 2 - textWidth / 2 - 20,
            startY - 60,
            screenWidth / 2 + textWidth / 2 + 20,
            startY + 20
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (startButton.contains(event.x, event.y)) {
                    isStartPressed = true
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isStartPressed && startButton.contains(event.x, event.y)) {
                    onStartGame()
                }
                isStartPressed = false
                return true
            }
        }
        return false
    }

    override fun cleanup() {
    }
}
