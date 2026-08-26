package com.icodelife.fcbee.scenes

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import com.icodelife.fcbee.sprites.*
import com.icodelife.fcbee.utils.ResourceManager
import com.icodelife.fcbee.utils.TextPainter
import kotlin.math.abs

/**
 * Main gameplay scene
 */
class PlayScene(
    private val screenWidth: Float,
    private val screenHeight: Float,
    private val onVictory: (score: Int) -> Unit,
    private val onDefeat: () -> Unit
) : Scene() {

    private lateinit var background: Bitmap
    private var backgroundY: Float = 0f
    private val backgroundScrollSpeed: Float = 100f

    private lateinit var fighter: Fighter
    private val bees = mutableListOf<Bee>()
    private val fighterBullets = mutableListOf<FighterBullet>()
    private val beeBullets = mutableListOf<BeeBullet>()
    private val explosions = mutableListOf<Explosion>()

    private var score: Int = 0
    private var lives: Int = 5
    private var beeShootTimer: Float = 0f
    private val beeShootInterval: Float = 1.0f

    private val scorePaint = TextPainter(40f, Color.WHITE)
    private val livesPaint = TextPainter(40f, Color.WHITE)

    private var touchX: Float = 0f
    private var touchY: Float = 0f
    private var isTouching: Boolean = false

    override fun init() {
        score = 0
        lives = 5
        beeShootTimer = 0f

        background = ResourceManager.getBitmap("background")!!
        backgroundY = 0f

        // Create fighter
        val fighterBitmap = ResourceManager.getBitmap("airplane")!!
        fighter = Fighter(
            screenWidth / 2,
            screenHeight - 150,
            fighterBitmap
        )

        // Create bees in formation
        createBees()

        // Clear all sprite lists
        fighterBullets.clear()
        beeBullets.clear()
        explosions.clear()
    }

    private fun createBees() {
        bees.clear()
        
        // Configuration: [frame type, y offset, count]
        val formations = listOf(
            Triple(2, 0, 6),      // Top row: 6 bees of type 2
            Triple(1, 80, 6),     // Middle row: 6 bees of type 1
            Triple(0, 160, 6),    // Row 3: 6 bees of type 0
            Triple(0, 240, 6),    // Row 4: 6 bees of type 0
            Triple(0, 320, 6)     // Bottom row: 6 bees of type 0
        )

        formations.forEach { (type, yOffset, count) ->
            val spacing = 80f
            val startX = (screenWidth - (count - 1) * spacing) / 2
            
            for (i in 0 until count) {
                val bee = Bee(
                    startX + i * spacing,
                    200f + yOffset,
                    ResourceManager.getBeeFrames(type),
                    type
                )
                bee.startX = bee.x
                bees.add(bee)
            }
        }
    }

    override fun update(deltaTime: Float) {
        // Scroll background
        backgroundY += backgroundScrollSpeed * deltaTime
        if (backgroundY >= screenHeight) {
            backgroundY = 0f
        }

        // Update fighter movement based on touch
        if (isTouching) {
            val targetX = touchX.coerceIn(fighter.width / 2, screenWidth - fighter.width / 2)
            val targetY = touchY.coerceIn(fighter.height / 2, screenHeight - fighter.height / 2)
            
            fighter.velocityX = (targetX - fighter.x) * 5f
            fighter.velocityY = (targetY - fighter.y) * 5f
        } else {
            fighter.velocityX = 0f
            fighter.velocityY = 0f
        }
        
        fighter.update(deltaTime)
        
        // Keep fighter in bounds
        fighter.x = fighter.x.coerceIn(fighter.width / 2, screenWidth - fighter.width / 2)
        fighter.y = fighter.y.coerceIn(fighter.height / 2, screenHeight - fighter.height / 2)

        // Update bees
        bees.forEach { it.update(deltaTime) }

        // Bee shooting
        beeShootTimer += deltaTime
        if (beeShootTimer >= beeShootInterval) {
            beeShootTimer = 0f
            bees.filter { it.isActive }.randomOrNull()?.let { bee ->
                val bullet = BeeBullet(
                    bee.x,
                    bee.y,
                    ResourceManager.getBitmap("bee_bullet")!!
                )
                bullet.fire(bee.x, bee.y, fighter.x, fighter.y)
                beeBullets.add(bullet)
            }
        }

        // Update bullets
        fighterBullets.forEach { it.update(deltaTime) }
        beeBullets.forEach { it.update(deltaTime) }
        
        // Remove inactive bullets
        fighterBullets.removeAll { !it.isActive }
        beeBullets.removeAll { !it.isActive }

        // Update explosions
        explosions.forEach { it.update(deltaTime) }
        explosions.removeAll { !it.isActive }

        // Collision detection
        checkCollisions()

        // Check victory/defeat
        checkGameState()
    }

    private fun checkCollisions() {
        // Fighter bullets hit bees
        val bulletsToRemove = mutableListOf<FighterBullet>()
        val beesToRemove = mutableListOf<Bee>()

        fighterBullets.forEach { bullet ->
            bees.forEach { bee ->
                if (bullet.isActive && bee.isActive && bullet.collidesWith(bee)) {
                    bulletsToRemove.add(bullet)
                    beesToRemove.add(bee)
                    createExplosion(bee.x, bee.y)
                    score += 100
                }
            }
        }

        bulletsToRemove.forEach { it.destroy() }
        beesToRemove.forEach { it.destroy() }
        bees.removeAll { !it.isActive }

        // Bee bullets hit fighter
        beeBullets.forEach { bullet ->
            if (bullet.isActive && fighter.isActive && bullet.collidesWith(fighter)) {
                bullet.destroy()
                lives--
                createExplosion(fighter.x, fighter.y)
            }
        }
    }

    private fun createExplosion(x: Float, y: Float) {
        val explosion = Explosion(x, y, ResourceManager.getExplosionFrames())
        explosion.show(x, y)
        explosions.add(explosion)
    }

    private fun checkGameState() {
        if (lives <= 0) {
            onDefeat()
        } else if (bees.isEmpty()) {
            onVictory(score)
        }
    }

    override fun draw(canvas: Canvas) {
        // Draw scrolling background
        canvas.drawBitmap(background, 0f, backgroundY - screenHeight, null)
        canvas.drawBitmap(background, 0f, backgroundY, null)

        // Draw sprites
        fighter.draw(canvas)
        bees.forEach { it.draw(canvas) }
        fighterBullets.forEach { it.draw(canvas) }
        beeBullets.forEach { it.draw(canvas) }
        explosions.forEach { it.draw(canvas) }

        // Draw UI
        scorePaint.drawText(canvas, "SCORE: $score", 30f, 60f)
        livesPaint.drawText(canvas, "LIVES: $lives", screenWidth - 230f, 60f)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                touchX = event.x
                touchY = event.y
                isTouching = true
                
                // Shoot bullet
                if (fighter.shoot()) {
                    val bullet = FighterBullet(
                        fighter.x,
                        fighter.y,
                        ResourceManager.getBitmap("bullet")!!
                    )
                    bullet.fire(fighter.x, fighter.y, fighter.x, 0f)
                    fighterBullets.add(bullet)
                }
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isTouching = false
                return true
            }
        }
        return false
    }

    override fun cleanup() {
        bees.clear()
        fighterBullets.clear()
        beeBullets.clear()
        explosions.clear()
    }
}
