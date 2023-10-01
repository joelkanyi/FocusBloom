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
package com.joelkanyi.focusbloom.feature.addtask

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.model.TaskType
import com.joelkanyi.focusbloom.core.domain.model.taskTypes
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import com.joelkanyi.focusbloom.core.utils.UiEvents
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddTaskScreenModel(
    settingsRepository: SettingsRepository,
    private val tasksRepository: TasksRepository,
) : ScreenModel {
    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow.asSharedFlow()

    val sessionTime = settingsRepository.getSessionTime()
        .map {
            it
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
    val shortBreakTime = settingsRepository.getShortBreakTime()
        .map { it }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
    val longBreakTime = settingsRepository.getLongBreakTime()
        .map { it }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
    val hourFormat = settingsRepository.getHourFormat()
        .map { it }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private val _focusSessions = MutableStateFlow(0)
    val focusSessions = _focusSessions.asStateFlow()
    fun incrementFocusSessions() {
        _focusSessions.value++
    }

    fun decrementFocusSessions() {
        if (_focusSessions.value > 0) {
            _focusSessions.value--
        }
    }

    private val _taskName = mutableStateOf("")
    val taskName: State<String> = _taskName
    fun setTaskName(name: String) {
        _taskName.value = name
    }

    private val _taskDescription = mutableStateOf("")
    val taskDescription: State<String> = _taskDescription
    fun setTaskDescription(description: String) {
        _taskDescription.value = description
    }

    private val _selectedOption = MutableStateFlow(taskTypes.last())
    val selectedOption = _selectedOption.asStateFlow()
    fun setSelectedOption(option: TaskType) {
        _selectedOption.value = option
    }

    private val _showStartTimeInputDialog = MutableStateFlow(false)
    val showStartTimeInputDialog = _showStartTimeInputDialog.asStateFlow()
    fun setShowStartTimeInputDialog(show: Boolean) {
        _showStartTimeInputDialog.value = show
    }

    private val _showTaskDatePickerDialog = MutableStateFlow(false)
    val showTaskDatePickerDialog = _showTaskDatePickerDialog.asStateFlow()
    fun setShowTaskDatePickerDialog(show: Boolean) {
        _showTaskDatePickerDialog.value = show
    }

    fun addTask(
        task: Task,
    ) {
        coroutineScope.launch {
            tasksRepository.addTask(task)
            _focusSessions.value = 0
            _taskName.value = ""
            _taskDescription.value = ""
            _selectedOption.value = taskTypes.last()
            _showStartTimeInputDialog.value = false
            _eventsFlow.emit(UiEvents.ShowSnackbar("Task added!"))
        }
    }
}
