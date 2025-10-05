# Android 项目构建指南

## 环境准备

### 必需工具

1. **Java Development Kit (JDK) 17**
   ```bash
   # 验证安装
   java -version
   ```

2. **Android Studio** (Arctic Fox 或更高版本)
   - 下载地址: https://developer.android.com/studio
   - 安装后首次运行会自动下载Android SDK

3. **Android SDK**
   - 最低版本: API 24 (Android 7.0)
   - 目标版本: API 34 (Android 14)
   - Android Studio会自动管理SDK

### 可选工具

- **Git**: 用于版本控制
- **Android设备或模拟器**: 用于测试

## 项目结构检查

确保以下文件存在：

```
android/
├── build.gradle (项目级)
├── settings.gradle
├── gradle.properties
├── gradle/wrapper/
│   └── gradle-wrapper.properties
├── app/
│   ├── build.gradle (应用级)
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── kotlin/com/icodelife/fcbee/
│       │   ├── MainActivity.kt
│       │   ├── GameView.kt
│       │   ├── scenes/
│       │   ├── sprites/
│       │   └── utils/
│       ├── res/
│       │   ├── drawable/
│       │   ├── values/
│       │   └── mipmap-*/
│       └── assets/
└── README.md
```

## 使用Android Studio构建

### 步骤1: 打开项目

1. 启动Android Studio
2. 选择 `File` → `Open`
3. 导航到 `fc-bee/android` 目录
4. 点击 `OK`

### 步骤2: Gradle同步

1. Android Studio会自动开始同步Gradle
2. 如果没有自动开始，点击 `File` → `Sync Project with Gradle Files`
3. 等待依赖下载完成（首次可能需要几分钟）

### 步骤3: 配置运行设备

**选项A: 使用真实设备**
1. 在Android设备上启用开发者选项和USB调试
2. 用USB线连接设备到电脑
3. 在Android Studio顶部工具栏选择你的设备

**选项B: 使用模拟器**
1. 点击 `Tools` → `Device Manager`
2. 点击 `Create Device`
3. 选择设备定义（推荐: Pixel 6）
4. 选择系统镜像（推荐: API 34）
5. 完成创建并启动模拟器

### 步骤4: 运行应用

1. 点击工具栏的绿色运行按钮（▶️）
2. 或按快捷键 `Shift + F10`
3. 应用将自动安装并启动

## 命令行构建

### 准备工作

确保已设置 `ANDROID_HOME` 环境变量：

```bash
# Linux/macOS
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools

# Windows (PowerShell)
$env:ANDROID_HOME = "C:\Users\YourUsername\AppData\Local\Android\Sdk"
```

### 构建Debug版本

```bash
cd android
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

生成的APK位于：`app/build/outputs/apk/debug/app-debug.apk`

### 构建Release版本

```bash
cd android
./gradlew assembleRelease
```

生成的APK位于：`app/build/outputs/apk/release/app-release-unsigned.apk`

**注意**: Release版本需要签名才能安装。

### 安装到设备

```bash
# 确保设备已连接
adb devices

# 安装Debug版本
./gradlew installDebug

# 安装Release版本（需要先签名）
./gradlew installRelease
```

### 清理构建

```bash
./gradlew clean
```

## 签名Release版本

### 创建密钥库

```bash
keytool -genkey -v -keystore my-release-key.keystore \
  -alias my-key-alias -keyalg RSA -keysize 2048 -validity 10000
```

### 配置签名

在 `app/build.gradle` 中添加：

```gradle
android {
    signingConfigs {
        release {
            storeFile file("my-release-key.keystore")
            storePassword "your_store_password"
            keyAlias "my-key-alias"
            keyPassword "your_key_password"
        }
    }
    
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

## 常见问题排查

### 问题1: Gradle同步失败

**解决方案:**
1. 检查网络连接
2. 在 `gradle.properties` 中添加代理（如果需要）
3. 删除 `.gradle` 目录并重新同步
4. 确保JDK版本正确（需要JDK 17）

### 问题2: 找不到SDK

**解决方案:**
1. 打开 `File` → `Project Structure` → `SDK Location`
2. 设置正确的Android SDK路径
3. 或创建 `local.properties` 文件：
   ```
   sdk.dir=/path/to/android/sdk
   ```

### 问题3: 应用图标缺失

**解决方案:**
1. 查看 `LAUNCHER_ICON_GUIDE.md`
2. 使用Android Studio的Image Asset工具生成图标
3. 或手动添加图标到mipmap目录

### 问题4: 资源找不到

**解决方案:**
1. 确保所有图片文件在 `res/drawable/` 目录
2. 检查文件名是否符合Android命名规范（小写字母、数字、下划线）
3. 运行 `Build` → `Clean Project` 然后 `Build` → `Rebuild Project`

### 问题5: 构建速度慢

**解决方案:**
在 `gradle.properties` 中添加：
```properties
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.caching=true
```

## 性能优化建议

1. **启用R8/ProGuard** (Release构建)
   ```gradle
   buildTypes {
       release {
           minifyEnabled true
           shrinkResources true
       }
   }
   ```

2. **使用APK Analyzer**
   - `Build` → `Analyze APK`
   - 分析APK大小和内容

3. **优化图片资源**
   - 使用WebP格式
   - 压缩PNG图片
   - 移除未使用的资源

## 测试

### 单元测试

```bash
./gradlew test
```

### 设备测试

```bash
./gradlew connectedAndroidTest
```

## 分发

### Google Play Store

1. 构建签名的Release版本
2. 在Google Play Console创建应用
3. 上传APK或使用App Bundle
4. 填写商店信息
5. 提交审核

### 其他方式

- 直接分发APK文件
- 使用第三方应用商店
- 使用Firebase App Distribution进行测试分发

## 持续集成

可以使用GitHub Actions进行自动构建：

```yaml
name: Android Build

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        java-version: '17'
        distribution: 'adopt'
    - name: Build with Gradle
      run: |
        cd android
        chmod +x gradlew
        ./gradlew assembleDebug
```

## 更多资源

- [Android开发者文档](https://developer.android.com/docs)
- [Kotlin语言指南](https://kotlinlang.org/docs/home.html)
- [Android Studio用户指南](https://developer.android.com/studio/intro)
- [Gradle构建工具](https://gradle.org/guides/)
