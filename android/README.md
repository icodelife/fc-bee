# FC Bee - Android Kotlin Version

这是经典FC小蜜蜂游戏的Android Kotlin版本，从PhaserJS3 + TypeScript重构而来。

## 项目结构

```
android/
├── app/
│   ├── src/main/
│   │   ├── kotlin/com/icodelife/fcbee/
│   │   │   ├── MainActivity.kt          # 主Activity
│   │   │   ├── GameView.kt              # 游戏视图和游戏循环
│   │   │   ├── scenes/                  # 游戏场景
│   │   │   │   ├── Scene.kt             # 场景基类和管理器
│   │   │   │   ├── StartScene.kt        # 开始场景
│   │   │   │   ├── PlayScene.kt         # 游戏场景
│   │   │   │   └── GameEndScenes.kt     # 胜利/失败场景
│   │   │   ├── sprites/                 # 游戏精灵
│   │   │   │   ├── Sprite.kt            # 精灵基类
│   │   │   │   ├── Bullet.kt            # 子弹类
│   │   │   │   └── GameSprites.kt       # 战斗机、蜜蜂、爆炸效果
│   │   │   └── utils/                   # 工具类
│   │   │       └── ResourceManager.kt   # 资源管理器
│   │   ├── res/                         # Android资源
│   │   │   ├── drawable/                # 图片资源
│   │   │   └── values/                  # 字符串、颜色等
│   │   ├── assets/                      # 字体等资源
│   │   └── AndroidManifest.xml          # Android清单文件
│   └── build.gradle                     # 应用级构建配置
├── build.gradle                         # 项目级构建配置
├── settings.gradle                      # 项目设置
└── gradle.properties                    # Gradle属性
```

## 游戏特性

- ✅ 经典FC小蜜蜂玩法
- ✅ 触摸控制：点击移动战斗机位置
- ✅ 自动射击系统
- ✅ 多种敌人类型（3种不同的小蜜蜂）
- ✅ 碰撞检测
- ✅ 爆炸动画效果
- ✅ 计分系统
- ✅ 生命值系统
- ✅ 背景滚动效果
- ✅ 胜利/失败场景

## 技术栈

- **语言**: Kotlin
- **最低SDK**: API 24 (Android 7.0)
- **目标SDK**: API 34 (Android 14)
- **架构**: 自定义2D游戏引擎
- **渲染**: Android Canvas + SurfaceView
- **游戏循环**: 60 FPS固定帧率

## 核心组件

### 1. GameView
主游戏视图，负责：
- 初始化游戏资源
- 管理游戏循环（60 FPS）
- 处理触摸事件
- 场景切换

### 2. Scene系统
- **StartScene**: 游戏开始界面
- **PlayScene**: 主游戏场景
- **VictoryScene**: 胜利界面
- **DefeatScene**: 失败界面

### 3. Sprite系统
- **Sprite**: 所有游戏对象的基类
- **Fighter**: 玩家战斗机
- **Bee**: 敌人小蜜蜂（支持动画）
- **Bullet**: 子弹基类
- **FighterBullet**: 战斗机子弹
- **BeeBullet**: 蜜蜂子弹
- **Explosion**: 爆炸效果

### 4. ResourceManager
统一管理游戏资源：
- 图片加载和缓存
- Spritesheet裁剪
- 自定义字体加载
- 资源缩放

## 构建说明

### 环境要求
- Android Studio Arctic Fox或更高版本
- JDK 17
- Android SDK 34
- Gradle 8.0+

### 构建步骤

1. 克隆仓库：
```bash
git clone https://github.com/icodelife/fc-bee.git
cd fc-bee/android
```

2. 使用Android Studio打开项目：
   - 打开Android Studio
   - 选择 "Open an Existing Project"
   - 选择 `android` 目录

3. 同步Gradle：
   - Android Studio会自动提示同步Gradle
   - 等待依赖下载完成

4. 运行应用：
   - 连接Android设备或启动模拟器
   - 点击 "Run" 按钮或按 `Shift+F10`

### 命令行构建

```bash
# 进入android目录
cd android

# 构建Debug版本
./gradlew assembleDebug

# 构建Release版本
./gradlew assembleRelease

# 安装到设备
./gradlew installDebug
```

生成的APK位于：`app/build/outputs/apk/`

## 游戏玩法

1. **开始游戏**: 点击"TAP TO START"开始游戏
2. **控制战斗机**: 点击屏幕任意位置，战斗机会移动到该位置
3. **射击**: 战斗机会自动射击
4. **目标**: 消灭所有小蜜蜂
5. **获胜条件**: 消灭所有敌人
6. **失败条件**: 生命值归零

### 计分规则
- 击中小蜜蜂：+100分
- 初始生命值：5条

## 与原版对比

### 原版 (PhaserJS3 + TypeScript)
- Web浏览器运行
- 键盘控制（WASD + 空格键）
- PhaserJS游戏引擎
- 750x1206分辨率

### Android版 (Kotlin)
- Android设备原生运行
- 触摸控制
- 自定义2D游戏引擎
- 自适应屏幕分辨率

## 后续改进计划

- [ ] 添加音效和背景音乐
- [ ] 添加更多关卡
- [ ] 添加不同的敌人攻击模式
- [ ] 添加道具系统（生命值、武器升级等）
- [ ] 添加本地最高分记录
- [ ] 添加暂停功能
- [ ] 优化触摸控制（添加虚拟摇杆选项）
- [ ] 添加设置界面

## 许可证

与原项目保持一致

## 致谢

原始PhaserJS3版本：[fc-bee](https://github.com/icodelife/fc-bee)

参考文章：[H5 游戏开发：FC 小蜜蜂](https://aotu.io/notes/2018/01/28/galaxian/)
