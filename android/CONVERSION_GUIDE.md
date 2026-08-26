# PhaserJS3 到 Kotlin Android 转换说明

## 概述

本文档说明如何将PhaserJS3 + TypeScript Web游戏转换为Kotlin Android原生应用。

## 架构对比

### PhaserJS3 架构
```
PhaserJS3 Game
├── Scenes (StartScene, PlayScene, VictoryScene, DefeatScene)
├── Sprites (Fighter, Bee, Bullet, Explosion)
├── Physics Engine (Arcade Physics)
├── Animation System
└── Input System (Keyboard)
```

### Android Kotlin 架构
```
Android App
├── MainActivity (Android Activity)
├── GameView (SurfaceView + Game Loop)
├── SceneManager (场景管理)
├── Scenes (同PhaserJS)
├── Sprites (自定义实现)
├── ResourceManager (资源管理)
└── 触摸输入系统
```

## 核心转换映射

| PhaserJS3 组件 | Android Kotlin 等价实现 |
|---------------|----------------------|
| `Phaser.Scene` | 自定义 `Scene` 基类 |
| `Phaser.GameObjects.Sprite` | 自定义 `Sprite` 基类 |
| `Phaser.Physics.Arcade.Sprite` | `Sprite` + 碰撞检测 |
| `Phaser.GameObjects.TileSprite` | 手动实现滚动背景 |
| `this.anims.create()` | 帧动画（切换Bitmap） |
| `this.tweens.add()` | 自定义移动逻辑 |
| `this.input.keyboard` | `onTouchEvent()` |
| `this.physics.add.collider()` | `RectF.intersects()` |

## 关键转换点

### 1. 游戏循环

**PhaserJS3:**
```typescript
update() {
  // 自动调用
}
```

**Kotlin:**
```kotlin
class GameThread : Thread() {
    override fun run() {
        while (running) {
            val deltaTime = calculateDeltaTime()
            gameView.update(deltaTime)
            // 渲染
        }
    }
}
```

### 2. 场景管理

**PhaserJS3:**
```typescript
this.scene.start('PlayScene', { score: 100 });
```

**Kotlin:**
```kotlin
sceneManager.setScene(playScene)
```

### 3. 精灵系统

**PhaserJS3:**
```typescript
class Fighter extends Phaser.Physics.Arcade.Sprite {
  constructor(scene, x, y, texture) {
    super(scene, x, y, texture);
  }
}
```

**Kotlin:**
```kotlin
class Fighter(
    x: Float,
    y: Float,
    bitmap: Bitmap
) : Sprite(x, y, width, height, bitmap) {
    // 实现
}
```

### 4. 动画系统

**PhaserJS3:**
```typescript
this.anims.create({
  key: 'fly',
  frames: this.anims.generateFrameNames('bee', { start: 0, end: 1 }),
  frameRate: 2,
  repeat: -1
});
sprite.play('fly');
```

**Kotlin:**
```kotlin
class Bee(frames: List<Bitmap>) : Sprite() {
    private var frameIndex = 0
    
    override fun update(deltaTime: Float) {
        frameIndex = (frameIndex + 1) % frames.size
        bitmap = frames[frameIndex]
    }
}
```

### 5. 碰撞检测

**PhaserJS3:**
```typescript
this.physics.add.collider(bullet, enemy, (b, e) => {
  // 碰撞处理
});
```

**Kotlin:**
```kotlin
fun checkCollisions() {
    bullets.forEach { bullet ->
        enemies.forEach { enemy ->
            if (bullet.collidesWith(enemy)) {
                // 碰撞处理
            }
        }
    }
}
```

### 6. 输入系统

**PhaserJS3:**
```typescript
// 键盘输入
this.cursors = this.input.keyboard.createCursorKeys();

if (this.cursors.left.isDown) {
  this.fighter.setVelocityX(-160);
}

// 空格键射击
this.input.keyboard.on('keyup_SPACE', () => {
  this.shoot();
});
```

**Kotlin:**
```kotlin
// 触摸输入
override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
        MotionEvent.ACTION_MOVE -> {
            // 移动战斗机到触摸位置
            fighter.x = event.x
            fighter.y = event.y
        }
        MotionEvent.ACTION_DOWN -> {
            // 自动射击
            shoot()
        }
    }
    return true
}
```

### 7. 资源加载

**PhaserJS3:**
```typescript
preload() {
  this.load.image('airplane', require('../images/airplane.png'));
  this.load.spritesheet('bee', require('../images/bee.png'), {
    frameWidth: 60,
    frameHeight: 40
  });
}
```

**Kotlin:**
```kotlin
object ResourceManager {
    fun init(context: Context) {
        bitmaps["airplane"] = loadBitmap("airplane")
        
        // 裁剪spritesheet
        val beeSheet = loadBitmap("bee")
        bitmaps["bee_0"] = cropBitmap(beeSheet, 0, 0, 60, 40)
        bitmaps["bee_1"] = cropBitmap(beeSheet, 60, 0, 60, 40)
    }
}
```

## 性能优化

### 对象池

**PhaserJS3:** 自动处理
```typescript
const bullets = this.physics.add.group({
  classType: Bullet,
  runChildUpdate: true
});
```

**Kotlin:** 需要手动实现
```kotlin
class BulletPool(private val maxSize: Int) {
    private val pool = mutableListOf<Bullet>()
    
    fun get(): Bullet {
        return pool.firstOrNull { !it.isActive }
            ?: createBullet().also { pool.add(it) }
    }
}
```

### 内存管理

- 及时回收未使用的Bitmap
- 使用对象池减少GC
- 避免在游戏循环中创建新对象

## 构建和部署

### Web版本 (PhaserJS3)
```bash
npm install
npm start        # 开发服务器
npm run build    # 生产构建
```

### Android版本 (Kotlin)
```bash
cd android
./gradlew assembleDebug     # Debug APK
./gradlew assembleRelease   # Release APK
./gradlew installDebug      # 安装到设备
```

## 常见问题

### Q: 为什么不使用现有的游戏引擎？
A: 为了保持项目的简洁性和学习目的，使用原生Android Canvas实现。如需更复杂的功能，可以考虑使用libGDX或Unity。

### Q: 如何添加音效？
A: 使用Android的SoundPool或MediaPlayer API：
```kotlin
val soundPool = SoundPool.Builder().setMaxStreams(10).build()
val soundId = soundPool.load(context, R.raw.shoot, 1)
soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
```

### Q: 如何支持不同屏幕尺寸？
A: 当前实现自动适配。可以通过缩放因子进一步优化：
```kotlin
val scale = screenWidth / 750f  // 基于原始宽度750
// 缩放所有精灵和位置
```

### Q: 如何优化帧率？
A: 
1. 使用对象池
2. 减少draw调用
3. 优化碰撞检测（使用空间分区）
4. 使用硬件加速

## 总结

将PhaserJS3游戏转换为Android Kotlin应用需要：

1. **重新实现游戏循环** - 使用Thread + SurfaceView
2. **自定义场景管理** - 不依赖游戏引擎
3. **实现精灵系统** - 包括碰撞检测、动画等
4. **适配输入系统** - 从键盘到触摸
5. **资源管理** - 加载和缓存图片资源
6. **性能优化** - 对象池、内存管理等

虽然需要更多手动实现，但可以获得：
- 原生性能
- 完全控制
- 无需依赖庞大的游戏引擎
- 更好的理解游戏开发原理
