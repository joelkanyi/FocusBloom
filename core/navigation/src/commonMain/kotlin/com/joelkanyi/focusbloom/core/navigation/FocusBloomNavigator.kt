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
package com.joelkanyi.focusbloom.core.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * A minimal, observable back stack the app shell renders. Features never touch this directly;
 * they expose navigation intents that the shell maps onto [navigate] / [pop].
 */
class FocusBloomNavigator(initial: FocusBloomDestination = FocusBloomDestination.Today) {
    private val _backStack = MutableStateFlow(listOf(initial))
    val backStack: StateFlow<List<FocusBloomDestination>> = _backStack.asStateFlow()

    val current: FocusBloomDestination get() = _backStack.value.last()

    fun navigate(destination: FocusBloomDestination) = _backStack.update { stack -> stack + destination }

    /** Clears the stack down to a single destination (top-level tab switches, post-onboarding). */
    fun replaceAll(destination: FocusBloomDestination) = _backStack.update { listOf(destination) }

    /** Pops the top entry; returns false when already at the root so the caller can exit. */
    fun pop(): Boolean {
        if (_backStack.value.size <= 1) return false
        _backStack.update { it.dropLast(1) }
        return true
    }
}
