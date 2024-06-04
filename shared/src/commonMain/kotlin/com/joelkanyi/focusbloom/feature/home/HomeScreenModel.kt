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
package com.joelkanyi.focusbloom.feature.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import com.joelkanyi.focusbloom.core.utils.plusDays
import com.joelkanyi.focusbloom.core.utils.today
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HomeScreenModel(
    private val tasksRepository: TasksRepository,
    private val settingsRepository: SettingsRepository,
) : ScreenModel {
    private val _openBottomSheet = MutableStateFlow(false)
    val openBottomSheet = _openBottomSheet.asStateFlow()
    fun openBottomSheet(value: Boolean) {
        _openBottomSheet.value = value
    }

    val hourFormat = settingsRepository.getHourFormat()
        .map { it ?: 24 }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 24,
        )

    val sessionTime = settingsRepository.getSessionTime()
        .map {
            it
        }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
    val shortBreakTime = settingsRepository.getShortBreakTime()
        .map { it }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
    val longBreakTime = settingsRepository.getLongBreakTime()
        .map { it }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    val remindersOn = settingsRepository.remindersOn()
        .map {
            ReminderState.Success(it)
        }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ReminderState.Loading,
        )

    fun deleteTask(task: Task) {
        screenModelScope.launch {
            tasksRepository.deleteTask(task.id)
        }
    }

    fun updateTask(task: Task) {
        screenModelScope.launch {
            tasksRepository.updateTask(task)
        }
    }

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask = _selectedTask.asStateFlow()
    fun selectTask(task: Task?) {
        _selectedTask.value = task
    }

    fun pushToTomorrow(task: Task) {
        screenModelScope.launch {
            tasksRepository.updateTask(
                task.copy(
                    date = task.date.plusDays(1),
                    start = task.start.plusDays(1),
                ),
            )
        }
    }

    fun pushToToday(task: Task) {
        screenModelScope.launch {
            tasksRepository.updateTask(
                task.copy(
                    date = today(),
                    start = today(),
                ),
            )
        }
    }

    fun markAsCompleted(task: Task) {
        screenModelScope.launch {
            tasksRepository.updateTaskCompleted(
                id = task.id,
                completed = true,
            )
            tasksRepository.updateTaskActive(
                id = task.id,
                active = false,
            )
            tasksRepository.updateTaskInProgress(
                id = task.id,
                inProgressTask = false,
            )
        }
    }

    val tasks = tasksRepository.getTasks()
        .map { tasks ->
            TasksState.Success(
                tasks = tasks
                    .sortedBy { it.start }
                    .filter {
                        it.date.date == Clock.System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date
                    },
                overdueTasks = tasks
                    .sortedBy { it.start }
                    .filter {
                        it.date.date < Clock.System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date &&
                            !it.completed
                    },
            )
        }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TasksState.Loading,
        )

    val username = settingsRepository.getUsername()
        .map { it }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    val shortBreakColor = settingsRepository.shortBreakColor()
        .map { it }
        .stateIn(
            screenModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val longBreakColor = settingsRepository.longBreakColor()
        .map { it }
        .stateIn(
            screenModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val focusColor = settingsRepository.focusColor()
        .map { it }
        .stateIn(
            screenModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    fun toggleReminder(value: Int) {
        screenModelScope.launch {
            settingsRepository.toggleReminder(value)
        }
    }
}

sealed class TasksState {
    data object Loading : TasksState()
    data class Success(
        val tasks: List<Task>,
        val overdueTasks: List<Task>,
    ) : TasksState()
}

sealed class ReminderState {
    data object Loading : ReminderState()
    data class Success(val reminderOn: Int?) : ReminderState()
}
