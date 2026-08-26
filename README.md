# FC 小蜜蜂 临摹

这是经典FC小蜜蜂游戏的复刻项目，提供两个版本：

1. **Web版本** - 使用 Phaserjs3 + TypeScript 开发
2. **Android版本** - 使用 Kotlin 原生开发

[参考 - H5 游戏开发：FC 小蜜蜂](https://aotu.io/notes/2018/01/28/galaxian/)

## Web版本启动 (PhaserJS3 + TypeScript)

```bash
npm install
npm start
```

在浏览器中打开 http://localhost:1234

![截图](https://s2.ax1x.com/2020/01/13/lHRD1A.jpg)

## Android版本 (Kotlin)

Android Kotlin原生版本位于 `android/` 目录。

### 快速开始

1. 使用Android Studio打开 `android/` 目录
2. 同步Gradle依赖
3. 连接Android设备或启动模拟器
4. 点击运行按钮

### 命令行构建

```bash
cd android
./gradlew assembleDebug
./gradlew installDebug
```

详细说明请参考 [android/README.md](android/README.md)

## 开发步骤

先列出大纲，详细文章正在整理中...，择日更新

### 初始化画布

### 初始化四个场景

建立四个场景： 开始、游戏、失败、成功

### 开始场景

图片与文本定位的差别

背景滚动

资源加载

进度条

点击事件

转转到游戏场景

### 游戏场景

初始化小蜜蜂

分三组

蜜蜂飞舞动画
整体移动动画

初始化飞机

飞机移动

子弹发射逻辑处理

碰撞逻辑处理

爆炸动画

加分与生命值

成功与失败转场

### 失败、成功场景

成功场景显示所得分数

场景之前传参
