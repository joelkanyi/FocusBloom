# Kotlin-Multiplatform-Template

A [kotlin multiplatform](https://kotlinlang.org/docs/multiplatform.html) project template.

## Prerequisite

// ToDo

## Project Structure

// ToDo

## Libraries

### __Shared__

- [Koin](https://insert-koin.io/docs/setup/v3.1) - Kotin dependency injection library with multiplatform support.
- [Ktor](https://ktor.io/docs/http-client-multiplatform.html) - Provides multiplatform libraries required to make network calls to the REST API.
- [SQLDelight](https://cashapp.github.io/sqldelight/multiplatform_sqlite/) - Cross-Platform database library
- [Realm](https://github.com/realm/realm-kotlin) - Caching of application data from network responses.
- [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings) - This is a Kotlin library for Multiplatform apps, so that common code can persist key-value data.
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Library support for Kotlin coroutines with multiplatform support.
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - Provides sets of libraries for various serialization formats eg. JSON, protocol buffers, CBOR etc.
- [kotlinx.datetime](https://github.com/Kotlin/kotlinx-datetime) - A multiplatform Kotlin library for working with date and time.
- [Napier](https://github.com/AAkira/Napier) -  Logging library for Kotlin Multiplatform.
- [Mockk](https://github.com/mockk/mockk) - Library for creating mocks for tests.

### Android

- [Jetpack Compose](https://developer.android.com/jetpack/compose?gclid=Cj0KCQiA95aRBhCsARIsAC2xvfwC4pw6JG3r8U_4zVVSzwfCSIMMM8MKPMGAOTRoMjpkfpimPVz1FwoaAqlUEALw_wcB&gclsrc=aw.ds) - Modern toolkit for building native UI.
- [Coil](https://coil-kt.github.io/coil/) - An image loading library for Android backed by kotlin coroutines.

### __Plugins__

- [KtLint](https://github.com/JLLeitschuh/ktlint-gradle) - This plugin creates convenient tasks in
  your Gradle project that run ktlint checks or do code auto format.
- [Detekt](https://github.com/detekt/detekt) - A static code analysis tool for the Kotlin
  programming language.
- [Gradle Versions Plugin](https://github.com/ben-manes/gradle-versions-plugin) - This plugin
  provides a task to determine which dependencies have updates.

## Sample Projects

1. [Notflix](https://github.com/VictorKabata/Notflix) - An android and desktop app built using Kotlin Multiplatforom that consumes TMDB API to display current trending, upcoming and popular movies🍿 and tvshows🎬.

## Learning Resources

1. [Official intro to Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
2. [Migrating to Kotlin DSL](https://evanschepsiror.medium.com/migrating-to-kotlin-dsl-4ee0d6d5c977) medium article by [Chepsi](https://twitter.com/_Chepsi?s=20&t=UaWQD6SYSIJ-TNnFzPBuBw).
3. [Make your Android application work on iOS](https://kotlinlang.org/docs/multiplatform-mobile-integrate-in-existing-app.html)tutorial.
4. [CocoaPods overview and setup](https://kotlinlang.org/docs/native-cocoapods.html) tutorial.
5. [Creating a cross-platform mobile application](https://ktor.io/docs/getting-started-ktor-client-multiplatform-mobile.html)official tutorial by the Ktor team.
6. [Using Koin in a Kotlin Multiplatform Project](https://johnoreilly.dev/posts/kotlinmultiplatform-koin/) article by [John O'Reilly](https://twitter.com/joreilly?s=20&t=T4SvCGuw53K_k1Lizc6VkQ).
7. GitHub README on [creating compose multiplatform desktop application](https://github.com/JetBrains/compose-jb/tree/master/tutorials/Getting_Started)
8. [GitHub Repository Template](https://github.com/wangerekaharun/GradleBuildPlugins) showcasing how to setup a gradle plugins by [Harun Wangereka](https://twitter.com/wangerekaharun?s=20&t=UaWQD6SYSIJ-TNnFzPBuBw).
9. [Official SQLDelight KMP Documentation](https://cashapp.github.io/sqldelight/multiplatform_sqlite/) on getting started with [SQLDelight](https://github.com/cashapp/sqldelight).
10. [Getting started with Compose Multiplatform](https://www.jetbrains.com/lp/compose-mpp/).
11. [Developing UI: Compose Multiplatform](https://www.raywenderlich.com/books/kotlin-multiplatform-by-tutorials/v1.0/chapters/5-developing-ui-compose-multiplatform) article by [Raywenderlich](https://www.raywenderlich.com/).
12. [Wrapping Kotlin Flow with Swift Combine Publisher in a Kotlin Multiplatform project](https://johnoreilly.dev/posts/kotlinmultiplatform-swift-combine_publisher-flow/) by [John O'Reilly](https://twitter.com/joreilly?s=20&t=T4SvCGuw53K_k1Lizc6VkQ).
13. [T-shaping for iOS Developers with Kotlin Multiplatform in 4 hours!](https://www.notion.so/T-shaping-for-iOS-Developers-with-Kotlin-Multiplatform-in-4-hours-87d2ea5884214e3297113da9c0912b2d)
   .
14. [Kotlin Multiplatform Mobile: what is it and when should you use it?](https://www.kinandcarta.com/en/insights/2022/01/what-is-kotlin-multiplatform-mobile-and-when-should-i-use-it/)
   blog post.
15. [Introduction to Kotlin Multiplatform Mobile - Getting Started](https://betterprogramming.pub/introduction-to-kotlin-multiplatform-mobile-part-i-333af55741af) article.

## Work In Progress

- [x] Update README
- [ ] Create sample projects - Work In Progress
