package com.icodelife.fcbee

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.icodelife.fcbee.scenes.*
import com.icodelife.fcbee.utils.ResourceManager

/**
 * Main game view that handles rendering and game loop
 */
class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var gameThread: GameThread? = null
    private val sceneManager = SceneManager()
    
    private var screenWidth: Float = 0f
    private var screenHeight: Float = 0f

    private lateinit var startScene: StartScene
    private lateinit var playScene: PlayScene
    private lateinit var victoryScene: VictoryScene
    private lateinit var defeatScene: DefeatScene

    init {
        holder.addCallback(this)
        isFocusable = true
        
        // Initialize resource manager
        ResourceManager.init(context)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        screenWidth = width.toFloat()
        screenHeight = height.toFloat()

        // Initialize scenes
        startScene = StartScene(screenWidth, screenHeight) {
            sceneManager.setScene(playScene)
        }

        playScene = PlayScene(screenWidth, screenHeight,
            onVictory = { score ->
                victoryScene.setScore(score)
                sceneManager.setScene(victoryScene)
            },
            onDefeat = {
                sceneManager.setScene(defeatScene)
            }
        )

        victoryScene = VictoryScene(screenWidth, screenHeight) {
            sceneManager.setScene(playScene)
        }

        defeatScene = DefeatScene(screenWidth, screenHeight) {
            sceneManager.setScene(playScene)
        }

        // Start with start scene
        sceneManager.setScene(startScene)

        // Start game thread
        gameThread = GameThread(holder, this)
        gameThread?.running = true
        gameThread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        screenWidth = width.toFloat()
        screenHeight = height.toFloat()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        gameThread?.running = false
        while (retry) {
            try {
                gameThread?.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun update(deltaTime: Float) {
        sceneManager.update(deltaTime)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        sceneManager.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return sceneManager.onTouchEvent(event)
    }

    /**
     * Game thread for game loop
     */
    class GameThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView) : Thread() {
        var running = false
        private val targetFPS = 60
        private val targetTime = 1000 / targetFPS

        override fun run() {
            var lastTime = System.currentTimeMillis()

            while (running) {
                val currentTime = System.currentTimeMillis()
                val deltaTime = (currentTime - lastTime) / 1000f
                lastTime = currentTime

                // Update game state
                gameView.update(deltaTime)

                // Draw
                var canvas: Canvas? = null
                try {
                    canvas = surfaceHolder.lockCanvas()
                    synchronized(surfaceHolder) {
                        canvas?.let { gameView.draw(it) }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    canvas?.let {
                        try {
                            surfaceHolder.unlockCanvasAndPost(it)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                // Maintain target frame rate
                val timeThisFrame = System.currentTimeMillis() - currentTime
                val timeToSleep = targetTime - timeThisFrame
                if (timeToSleep > 0) {
                    try {
                        sleep(timeToSleep)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
