# 应用图标说明

## 当前状态

当前项目未包含应用启动图标（launcher icons）。AndroidManifest.xml中引用了 `@mipmap/ic_launcher`，但该资源文件尚未创建。

## 如何添加应用图标

### 方法1：使用Android Studio的Image Asset工具（推荐）

1. 在Android Studio中打开项目
2. 右键点击 `app/src/main/res` 目录
3. 选择 `New` → `Image Asset`
4. 在打开的对话框中：
   - Asset Type: 选择 `Launcher Icons (Adaptive and Legacy)`
   - Name: 保持 `ic_launcher`
   - 选择图标源：
     - **Clip Art**: 从内置图标库选择
     - **Image**: 上传自定义图片（推荐512x512 PNG）
     - **Text**: 使用文字作为图标
5. 配置前景和背景（如果使用Adaptive图标）
6. 点击 `Next`，然后点击 `Finish`

这将自动生成所有需要的分辨率版本：
- `mipmap-mdpi/ic_launcher.png` (48x48)
- `mipmap-hdpi/ic_launcher.png` (72x72)
- `mipmap-xhdpi/ic_launcher.png` (96x96)
- `mipmap-xxhdpi/ic_launcher.png` (144x144)
- `mipmap-xxxhdpi/ic_launcher.png` (192x192)

### 方法2：手动创建图标

1. 准备不同尺寸的图标：
   - mdpi: 48x48 px
   - hdpi: 72x72 px
   - xhdpi: 96x96 px
   - xxhdpi: 144x144 px
   - xxxhdpi: 192x192 px

2. 将图标文件命名为 `ic_launcher.png`，并放置到对应的mipmap目录：
   ```
   app/src/main/res/
   ├── mipmap-mdpi/ic_launcher.png
   ├── mipmap-hdpi/ic_launcher.png
   ├── mipmap-xhdpi/ic_launcher.png
   ├── mipmap-xxhdpi/ic_launcher.png
   └── mipmap-xxxhdpi/ic_launcher.png
   ```

### 方法3：使用在线工具

1. 访问图标生成网站（如 https://romannurik.github.io/AndroidAssetStudio/）
2. 上传你的图标图片或使用工具设计
3. 下载生成的所有尺寸的图标
4. 将文件复制到对应的mipmap目录

## 图标建议

对于FC小蜜蜂游戏，建议使用：
- 游戏中的战斗机图片
- 小蜜蜂图片
- 或者设计一个结合两者的图标

可以从项目中的现有资源创建：
- `app/src/main/res/drawable/airplane.png` - 战斗机
- `app/src/main/res/drawable/galaxing.png` - 小蜜蜂

## 临时解决方案

在添加自定义图标之前，应用将使用Android系统的默认图标。这不会影响应用的功能，只是启动器中的图标不够美观。

## Adaptive Icons (Android 8.0+)

如果想要支持Adaptive Icons（自适应图标），还需要创建：

```xml
<!-- res/mipmap-anydpi-v26/ic_launcher.xml -->
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@color/ic_launcher_background"/>
    <foreground android:drawable="@mipmap/ic_launcher_foreground"/>
</adaptive-icon>
```

这允许图标在不同的设备和启动器上有更好的显示效果。
