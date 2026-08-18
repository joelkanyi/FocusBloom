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
package com.joelkanyi.focusbloom.shell

import org.koin.core.Koin
import org.koin.core.context.startKoin

/**
 * Starts Koin for iOS. The SwiftUI app touches [koin] once at launch (`DiModule.koin`) to
 * assemble the graph before the first screen renders.
 */
object DiModule {
    val koin: Koin = startKoin {
        modules(appModule, iosAppModule)
    }.koin
}
