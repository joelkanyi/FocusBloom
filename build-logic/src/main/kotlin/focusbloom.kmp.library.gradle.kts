import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

/**
 * Base Kotlin Multiplatform library convention: the four first-class targets
 * (Android, Desktop/JVM, iOS, Web/Wasm), JVM 17, namespace derived from the
 * project path. Applied by every :core:* and :capability:* module.
 */
plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    jvmToolchain(17)

    androidLibrary {
        namespace = "com.joelkanyi.focusbloom" + path.replace(":", ".")
        compileSdk = 36
        minSdk = 24
    }

    jvm("desktop")

    iosArm64()
    iosSimulatorArm64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
}
