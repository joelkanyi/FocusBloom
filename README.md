<p align="center"><img src="art/app_logo.png" alt="MealTime" height="150px"></p>

# FocusBloom
FocusBloom is a Kotlin Multiplatform app that helps users enhance their productivity and time management skills through focused work intervals and short breaks.

## 🛠️ WIP 🛠️
> Please note that this project is still under development and some features may not work as expected.

> If you find any bugs or have any suggestions, feel free to open an issue or a pull request.

## Platforms
![](https://img.shields.io/badge/Android-black.svg?style=for-the-badge&logo=android) | ![](https://img.shields.io/badge/iOS-black.svg?style=for-the-badge&logo=apple) | ![](https://img.shields.io/badge/Desktop-black.svg?style=for-the-badge&logo=windows) | ![](https://img.shields.io/badge/Web-black.svg?style=for-the-badge&logo=google-chrome)
:----: | :----: | :----: | :----:
✅ | ✅ | ✅ | Planned
<a href='https://play.google.com/store/apps/details?id=com.joelkanyi.focusbloom.android'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' height='80px'/></a>

## Screenshots
### Android
<img src="art/android_screen1.jpeg"  width="250"/> <img src="art/android_screen2.jpeg"  width="250"/> <img src="art/android_screen3.jpeg" width="250"/>

### iOS
<img src="art/ios_screen1.png"  width="250"/> <img src="art/ios_screen2.png"  width="250"/> <img src="art/ios_screen3.png"  width="250"/> 

### Desktop
<img src="art/dsk_screen1.png"/>
<img src="art/dsk_screen2.png"/>
<img src="art/dsk_screen3.png"/>

## Architecture
The app is shared between Android, iOS and Desktop. The shared code is written in Kotlin and the UI is built with Compose Multiplatform. Shared code, written in Kotlin, is compiled to JVM bytecode for Android and Desktop with Kotlin/JVM and to native binaries for iOS with Kotlin/Native.
### Modules
- shared:
  - contains all the shared code between the platforms
- android:
  - contains the android app
- ios:
  - contains the ios app 
- desktop:
  - contains the desktop app 

## Built with
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) - The Kotlin Multiplatform technology is designed to simplify the development of cross-platform projects.
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) -  a modern UI framework for Kotlin that makes building performant and beautiful user interfaces easy and enjoyable.
- [SQLDelight](https://github.com/cashapp/sqldelight) - SQLDelight is an open-source library developed by Cash App (formerly Square, Inc.) for working with SQL databases in Kotlin-based Android and multi-platform applications.
- [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings) - A Kotlin Multiplatform library for saving simple key-value data.
- [Koin](https://insert-koin.io/) - The pragmatic Kotlin & Kotlin Multiplatform Dependency Injection framework.
- [Voyager](https://voyager.adriel.cafe/) - A multiplatform navigation library.
- [Kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) - KotlinX multiplatform date/time library.
- [Kotlinx-serilization](https://github.com/Kotlin/kotlinx.serialization) - Kotlin multiplatform / multi-format serialization.
- [Koala plot](https://github.com/KoalaPlot/koalaplot-core) - Koala Plot is a Compose Multiplatform based charting and plotting library written in Kotlin.
- [Compose Components Resources](https://mvnrepository.com/artifact/org.jetbrains.compose.components/components-resources) - Resources For Compose Multiplatform.
- [Material3 Window Size Multiplatform](https://github.com/chrisbanes/material3-windowsizeclass-multiplatform) - About Material 3 Window Size Class for Compose Multiplatform.
- [Spotless](https://github.com/diffplug/spotless) - A code formatter that helps keep the codebase clean.
- [Github Actions](https://docs.github.com/en/actions) - A CI/CD tool that helps automate workflows.
- [Renovate](https://docs.renovatebot.com/) - An open-source software tool designed to help automate the process of updating dependencies in software projects.

## Run project
### Android
To run the application on android device/emulator:
- open project in Android Studio and run imported android run configuration

### Desktop
Run the desktop application: `./gradlew :desktop:run`

### iOS
To run the application on iPhone device/simulator:
- Open `ios/iosApp.xcworkspace` in Xcode and run standard configuration
- Or use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile) for Android Studio

## License
```xml
Copyright 2023 JoelKanyi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
```  
  
