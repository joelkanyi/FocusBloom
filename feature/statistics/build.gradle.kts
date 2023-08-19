plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinX.serialization.plugin)
    alias(libs.plugins.sqlDelight.plugin)
}

android {
    namespace = "com.joelkanyi.focusbloom.statistics"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
}

kotlin {
    jvm()
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.koin.core)

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