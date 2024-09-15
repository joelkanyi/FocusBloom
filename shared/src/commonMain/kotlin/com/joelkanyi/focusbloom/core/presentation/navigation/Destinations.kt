/*
 * Copyright 2024 Joel Kanyi.
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
package com.joelkanyi.focusbloom.core.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Destinations {
    @Serializable
    object Onboarding

    @Serializable
    object Username

    @Serializable
    object Home

    @Serializable
    data class AllTasks(val type: String)

    @Serializable
    data class AddTask(val taskId: Int = -1)

    @Serializable
    object Calendar

    @Serializable
    object Statistics

    @Serializable
    object AllStatistics

    @Serializable
    object Settings

    @Serializable
    data class TaskProgress(val taskId: Int)
}
