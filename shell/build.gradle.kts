/*
 * Copyright 2026 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
plugins {
    id("focusbloom.kmp.library")
    id("focusbloom.compose")
    alias(libs.plugins.nativeCocoapod)
}

kotlin {
    // :shell is the iOS umbrella framework, exported to the Xcode app as "shared" so the
    // existing Swift (import shared, MainKt.MainViewController(), DiModule.koin) is unchanged.
    cocoapods {
        version = "1.0"
        summary = "FocusBloom shared iOS framework"
        homepage = "https://github.com/joelkanyi/FocusBloom"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../ios/Podfile")
        framework {
            baseName = "shared"
            isStatic = false
        }
    }

    // The SQLDelight native driver (sqliter) needs the system SQLite linked into the framework.
    targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java) {
        binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class.java) {
            linkerOpts("-lsqlite3")
        }
    }

    sourceSets {
        commonMain.dependencies {
            // The shared app root. Consumes the design system (which brings Jenga + Compose
            // transitively) and navigation. Rendered by each platform launcher.
            api(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.common)

            // The shared composition graph: :shell binds capability impls and feature view
            // models so every platform launcher only adds its platform pieces.
            implementation(projects.feature.focus)
            implementation(projects.core.database)
            implementation(projects.capability.session.api)
            implementation(projects.capability.session.impl)
            implementation(projects.capability.items.api)
            implementation(projects.capability.items.impl)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
        // :shell is also the iOS umbrella framework, so the iOS composition root (the entry
        // point and its Koin graph) lives here. These deps are iOS-only and do not leak to the
        // Android, Desktop, or Web launchers, which bring their own graphs.
        iosMain.dependencies {
            implementation(projects.core.database)
            implementation(projects.core.datastore)
            implementation(libs.koin.core)
            implementation(libs.native.driver)
            implementation(libs.multiplatformSettings.base)
        }
    }
}
