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
@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    wasmJs {
        outputModuleName.set("focusbloom")
        browser {
            commonWebpackConfig {
                outputFileName = "focusbloom.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        wasmJsMain.dependencies {
            implementation(projects.shell)
            implementation(projects.core.designsystem)
            implementation(projects.core.common)
            implementation(projects.core.datastore)

            // Compose runtime/ui come transitively via :shell -> :core:designsystem -> Jenga.
            implementation(libs.koin.core)
            implementation(libs.multiplatformSettings.base)
            // Web StorageSettings is not observable on its own; makeObservable() wraps it.
            implementation(libs.multiplatformSettings.makeObservable)
        }
    }
}
