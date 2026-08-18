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
package com.joelkanyi.focusbloom.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Injectable coroutine dispatchers. Real code depends on this interface, not on
 * [Dispatchers] directly, so tests can swap in a single deterministic dispatcher.
 */
interface DispatcherProvider {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}

/** IO has no common definition (absent on Web/JS), so it is per-platform. */
internal expect val ioDispatcher: CoroutineDispatcher

class DefaultDispatcherProvider : DispatcherProvider {
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = ioDispatcher
}
