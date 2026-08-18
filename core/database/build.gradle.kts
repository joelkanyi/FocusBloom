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
    alias(libs.plugins.sqlDelight.plugin)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.sqlDelight.runtime)
            api(libs.coroutines.extensions)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.primitive.adapters)
            // Adapts the async-generated schema for the synchronous drivers (see synchronous()).
            implementation(libs.sqlDelight.async.extensions)
        }
    }
}

sqldelight {
    databases {
        create("FocusBloomDatabase") {
            packageName.set("com.joelkanyi.focusbloom.core.database")
            // Async generation is required for the Web (wasm) web-worker driver;
            // adopting it everywhere keeps one query API across all four targets.
            generateAsync.set(true)
        }
    }
}
