/*
 * Copyright 2023 Joel Kanyi.
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
package com.joelkanyi.focusbloom.core.domain.model

import kotlinx.datetime.LocalDateTime

data class Task(
    val id: Int = 0,
    val name: String,
    val description: String? = null,
    val type: String,
    val start: LocalDateTime,
    val color: Long,
    val current: String,
    val date: LocalDateTime,
    val focusSessions: Int,
    val currentCycle: Int,
    val completed: Boolean,
    val consumedFocusTime: Long,
    val consumedShortBreakTime: Long,
    val consumedLongBreakTime: Long,
    val inProgressTask: Boolean,
    val active: Boolean,
)
