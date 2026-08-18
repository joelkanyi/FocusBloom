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

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation destinations, owned by this leaf module so no feature depends on another.
 * Serializable so the back stack survives process death. The five top-level destinations plus
 * onboarding and the focus session; more arrive as features land.
 */
@Serializable
sealed interface FocusBloomDestination {
    @Serializable
    data object Today : FocusBloomDestination

    @Serializable
    data object Focus : FocusBloomDestination

    @Serializable
    data object Plan : FocusBloomDestination

    @Serializable
    data object Insights : FocusBloomDestination

    @Serializable
    data object You : FocusBloomDestination

    @Serializable
    data object Onboarding : FocusBloomDestination

    @Serializable
    data class Session(val taskId: Long? = null) : FocusBloomDestination
}
