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
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.joelkanyi.focusbloom.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.joelkanyi.focusbloom"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "2.0.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(projects.shell)
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.core.database)
    implementation(projects.core.datastore)

    implementation(libs.koin.android)
    implementation(libs.compose.activity)
    implementation(libs.android.driver)
    implementation(libs.multiplatformSettings.base)
}
