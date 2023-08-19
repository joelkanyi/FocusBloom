plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinX.serialization.plugin)
    alias(libs.plugins.sqlDelight.plugin)
    alias(libs.plugins.nativeCocoapod)
}

android {
    namespace = "com.joelkanyi.focusbloom.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../ios/Podfile")
        framework {
            baseName = "shared"
        }
    }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework> {
            transitiveExport = true
            compilations.all {
                kotlinOptions.freeCompilerArgs += arrayOf("-linker-options", "-lsqlite3")
            }
            export(project(":feature:settings"))
            export(project(":feature:statistics"))
            export(project(":feature:calendar"))
            export(project(":feature:home"))
            export(project(":core:common"))
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core:common"))
                api(project(":feature:settings"))
                api(project(":feature:statistics"))
                api(project(":feature:calendar"))
                api(project(":feature:home"))
                api(libs.koin.core)

                // api(libs.ktor.core)
                // api(libs.ktor.cio)
                // implementation(libs.ktor.contentNegotiation)
                // implementation(libs.ktor.json)
                // implementation(libs.ktor.logging)

                implementation(libs.kotlinX.serializationJson)

                implementation(libs.sqlDelight.runtime)
                implementation(libs.sqlDelight.coroutine)

                implementation(libs.multiplatformSettings.noArg)
                implementation(libs.multiplatformSettings.coroutines)

                api(libs.napier)

                implementation(libs.kotlinX.dateTime)
            }
        }

        val commonTest by getting {
            dependencies {
                dependsOn(commonMain)
                implementation(libs.kotlin.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.sqlDelight.android)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.sqlDelight.jvm)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosArm64Main.dependsOn(this)
            iosX64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                // implementation(libs.sqlDelight.native)
            }
        }
    }
}

sqldelight {
    database(name = "AppDatabase") {
        packageName = "com.joelkanyi.focusbloom.data.cache.sqldelight"
        sourceFolders = listOf("kotlin")
    }
}
