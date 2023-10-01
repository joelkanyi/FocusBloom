<p align="center"><img src="art/app_logo.png" alt="MealTime" height="150px"></p>

# FocusBloom
FocusBloom is a Kotlin Multiplatform Pomodoro app that helps users enhance their productivity and time management skills through focused work intervals and short breaks.

## Platforms
![](https://img.shields.io/badge/Android-black.svg?style=for-the-badge&logo=android) | ![](https://img.shields.io/badge/iOS-black.svg?style=for-the-badge&logo=apple) | ![](https://img.shields.io/badge/Desktop-black.svg?style=for-the-badge&logo=windows) | ![](https://img.shields.io/badge/Web-black.svg?style=for-the-badge&logo=google-chrome)
---- | ---- | ---- | ----
✅ | ✅ | ✅ | Planned

## Screenshots
### Android
<img src="art/app_screen1.jpeg"  width="250"/> <img src="art/app_screen2.jpeg"  width="250"/> <img src="art/app_screen3.jpeg"  width="250/">  <img src="art/app_screen4.jpeg"  width="250"/> <img src="art/app_screen5.jpeg"  width="250"/> 

### iOS
<img src="art/ios_screen1.png"  width="250"/> <img src="art/ios_screen2.png"  width="250"/> <img src="art/ios_screen3.png"  width="250"/> <img src="art/ios_screen4.png"  width="250"/> <img src="art/ios_screen5.png"  width="250"/> <img src="art/ios_screen6.png"  width="250"/> <img src="art/ios_screen7.png"  width="250"/> <img src="art/ios_screen8.png"  width="250"/> <img src="art/ios_screen9.png"  width="250"/>

### Desktop
<img src="art/dsk_screen1.png"/>
<img src="art/dsk_screen2.png"/>
<img src="art/dsk_screen3.png"/>
<img src="art/dsk_screen4.png"/>
<img src="art/dsk_screen5.png"/>
## Architecture
The app is shared between Android, iOS and Desktop. The shared code is written in Kotlin and the UI is built with Compose Multiplatform. The shared code is compiled to Kotlin/JVM for Android and Kotlin/Native for iOS and Desktop.


### Modules
- shared:
  - contains all the shared code between the platforms
  - contains the business logic and data layer
  - contains the UI layer
  - contains the database layer
  - contains the repository layer
- android: contains the android app
  - contains the android app
- ios: contains the ios app
  - contains the ios app 
- desktop: contains the desktop app
  - contains the desktop app 

## Built with
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) - Kotlin Multiplatform Mobile (KMM) is an SDK that allows you to use the same business logic code in both iOS and Android applications.
- [Compose Multiplatform]()
- [SQLDelight]() - SQLDelight generates typesafe Kotlin APIs from your SQL statements. It allows you to easily query your data and eliminates a lot of boilerplate code you'd normally have to write.

## Run project
### Android
To run the application on android device/emulator:
- open project in Android Studio and run imported android run configuration

To build the application bundle:
- run `./gradlew :composeApp:assembleDebug`
- find `.apk` file in `composeApp/build/outputs/apk/debug/composeApp-debug.apk`

### Desktop
Run the desktop application: `./gradlew :desktop:run`

### iOS
To run the application on iPhone device/simulator:
- Open `ios/iosApp.xcworkspace` in Xcode and run standard configuration
- Or use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile) for Android Studio

## Todo
- [ ] Work on Notifications: To remind you of upcoming and overdue tasks
- [ ] Reminders: Sounds for breaks and work sessions
- [ ] 
## Credits
- 
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
  
