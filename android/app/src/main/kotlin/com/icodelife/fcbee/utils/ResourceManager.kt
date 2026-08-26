package com.icodelife.fcbee.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Typeface
import java.io.IOException

/**
 * Resource manager for loading and managing game assets
 */
object ResourceManager {
    private lateinit var context: Context
    private val bitmaps = mutableMapOf<String, Bitmap>()
    private var customFont: Typeface? = null

    fun init(ctx: Context) {
        context = ctx
        loadAssets()
    }

    private fun loadAssets() {
        // Load bitmaps from drawable resources
        bitmaps["background"] = loadBitmap("galaxing_bg")
        bitmaps["airplane"] = loadBitmap("airplane")
        bitmaps["bullet"] = loadBitmap("bullet")
        bitmaps["bee_bullet"] = loadBitmap("enemy_bullet")
        
        // Load spritesheet for bees (galaxing)
        val galaxingSheet = loadBitmap("galaxing")
        bitmaps["bee_0_1"] = cropBitmap(galaxingSheet, 0, 0, 60, 40)
        bitmaps["bee_0_2"] = cropBitmap(galaxingSheet, 60, 0, 60, 40)
        bitmaps["bee_1_1"] = cropBitmap(galaxingSheet, 120, 0, 60, 40)
        bitmaps["bee_1_2"] = cropBitmap(galaxingSheet, 180, 0, 60, 40)
        bitmaps["bee_2_1"] = cropBitmap(galaxingSheet, 240, 0, 60, 40)
        bitmaps["bee_2_2"] = cropBitmap(galaxingSheet, 300, 0, 60, 40)
        
        // Load explosion frames
        val explodeSheet = loadBitmap("explode")
        for (i in 0 until 16) {
            val row = i / 4
            val col = i % 4
            bitmaps["explode_$i"] = cropBitmap(explodeSheet, col * 128, row * 128, 128, 128)
        }

        // Load custom font
        try {
            customFont = Typeface.createFromAsset(context.assets, "FC.ttf")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadBitmap(name: String): Bitmap {
        val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
        return BitmapFactory.decodeResource(context.resources, resId)
    }

    private fun cropBitmap(source: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(source, x, y, width, height)
    }

    fun getBitmap(name: String): Bitmap? {
        return bitmaps[name]
    }

    fun getBeeFrames(type: Int): List<Bitmap> {
        return listOf(
            bitmaps["bee_${type}_1"]!!,
            bitmaps["bee_${type}_2"]!!
        )
    }

    fun getExplosionFrames(): List<Bitmap> {
        return (0 until 16).map { bitmaps["explode_$it"]!! }
    }

    fun getFont(): Typeface? {
        return customFont
    }

    fun scaleBitmap(bitmap: Bitmap, scale: Float): Bitmap {
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}

/**
 * Text painter helper
 */
class TextPainter(size: Float, color: Int) {
    val paint = Paint().apply {
        textSize = size
        this.color = color
        textAlign = Paint.Align.LEFT
        isAntiAlias = true
        ResourceManager.getFont()?.let { typeface = it }
    }

    fun drawText(canvas: Canvas, text: String, x: Float, y: Float) {
        canvas.drawText(text, x, y, paint)
    }

    fun drawCenteredText(canvas: Canvas, text: String, centerX: Float, y: Float) {
        val textWidth = paint.measureText(text)
        canvas.drawText(text, centerX - textWidth / 2, y, paint)
    }
}
