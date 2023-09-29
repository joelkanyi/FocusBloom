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
package com.joelkanyi.focusbloom.feature.calendar

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class CalendarScreenModel(
    tasksRepository: TasksRepository,
    settingsRepository: SettingsRepository,
) : ScreenModel {
    private val _selectedDay = MutableStateFlow(
        Clock.System.now().toLocalDateTime(
            TimeZone.currentSystemDefault(),
        ).date,
    )
    val selectedDay = _selectedDay.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Clock.System.now().toLocalDateTime(
            TimeZone.currentSystemDefault(),
        ).date,
    )

    fun setSelectedDay(date: kotlinx.datetime.LocalDate) {
        _selectedDay.value = date
    }

    val tasks = tasksRepository.getTasks()
        .map { tasks ->
            tasks.sortedByDescending {
                it.date
            }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )

    val hourFormat = settingsRepository.getHourFormat()
        .map { it }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
}
