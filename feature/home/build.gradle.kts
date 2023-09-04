plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinX.serialization.plugin)
    alias(libs.plugins.nativeCocoapod)
    alias(libs.plugins.sqlDelight.plugin)
    alias(libs.plugins.compose.multiplatform)
}

android {
    namespace = "com.joelkanyi.focusbloom.home"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/commonMain/resources")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
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

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../../ios/Podfile")
        framework {
            baseName = "home"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core:common"))
                api(project(":feature:settings"))
                api(project(":feature:statistics"))
                api(project(":feature:calendar"))
                api(libs.koin.core)

                implementation(compose.material3)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)

                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.voyager.navigator)
                implementation(libs.voyager.bottomSheetNavigator)
                implementation(libs.voyager.transitions)
                implementation(libs.voyager.tabNavigator)
                // implementation(libs.voyager.koin)

                // api(libs.ktor.core)
                // api(libs.ktor.cio)
                // implementation(libs.ktor.contentNegotiation)
                // implementation(libs.ktor.json)
                // implementation(libs.ktor.logging)

                implementation(libs.kotlinX.serializationJson)

                implementation("dev.chrisbanes.material3:material3-window-size-class-multiplatform:0.3.1")

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
                implementation(libs.components.resources)
            }
        }
    }
}