# Biliw
不仅仅是第三方 Bilibili 手表客户端，还能支持其他M3U8源的播放。

# Features
1. 使用原生的组件库而不是 Jitpack Compose ，得以提供流畅的用户界面(UI)，较大程度避免掉帧  
2. 即使不看B站，也可以当作视频播放器使用 (目前仅支持从网络播放如M3U8等视频流，但也可以尝试使用 `file:` 协议填写视频绝对路径）  
3. 播放器支持长按加速，左右滑动跳转进度 (尚不支持调整亮度、音量，但其源代码在 [`PlayerControllerComponent.kt`](https://github.com/Klrohias/Biliw/blob/main/app/src/main/java/el/sft/bw/components/PlayerControllerComponent.kt)，可以自己调整定制)
4. 软件完全免费，无需支付任何费用单独购买播放器软件，以宽松的许可证开源以供其他人修改出符合自己意愿的版本。

# Build
```shell
# debug build:
./gradlew assembleDebug

# release build (the following environment variables are optional):
export KEY_PATH=/path/to/your.keystore
export KEY_PASS=[keystore_password]
export ALIAS_NAME=[alias_name]
export ALIAS_PASS=[alias_password]
./gradlew assembleRelease
```

# Reference
https://github.com/SocialSisterYi/bilibili-API-collect  
https://github.com/ikew0ng/SwipeBackLayout  

# License
[Apache 2.0](LICENSE)
