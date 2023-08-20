pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "FocusBloom"
include(":android")
include(":desktop")
include(":shared")
include(":core")
include(":core:designsystem")
include(":core:common")
include(":core:data")
include(":feature")
include(":feature:settings")
include(":feature:statistics")
include(":feature:calendar")
include(":feature:home")