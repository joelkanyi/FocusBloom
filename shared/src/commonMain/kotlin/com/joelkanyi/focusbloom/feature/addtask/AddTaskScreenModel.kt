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
import cafe.adriel.voyager.core.model.screenModelScope
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.model.TaskType
import com.joelkanyi.focusbloom.core.domain.model.taskTypes
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import com.joelkanyi.focusbloom.core.utils.UiEvents
import com.joelkanyi.focusbloom.core.utils.calculateFromFocusSessions
import com.joelkanyi.focusbloom.core.utils.today
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class AddTaskScreenModel(
    settingsRepository: SettingsRepository,
    private val tasksRepository: TasksRepository,
) : ScreenModel {
    private val _eventsFlow = Channel<UiEvents>(Channel.UNLIMITED)
    val eventsFlow = _eventsFlow.receiveAsFlow()

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
    val hourFormat = settingsRepository.getHourFormat()
        .map { it }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private val _focusSessions = MutableStateFlow(1)
    val focusSessions = _focusSessions.asStateFlow()
    fun incrementFocusSessions() {
        _focusSessions.value++
        setEndTime(
            calculateFromFocusSessions(
                focusSessions = focusSessions.value,
                sessionTime = sessionTime.value ?: 25,
                shortBreakTime = shortBreakTime.value ?: 5,
                longBreakTime = longBreakTime.value ?: 15,
                currentLocalDateTime = LocalDateTime(
                    year = taskDate.value.year,
                    month = taskDate.value.month,
                    dayOfMonth = taskDate.value.dayOfMonth,
                    hour = startTime.value.hour,
                    minute = startTime.value.minute,
                ),
            ),
        )
    }

    fun decrementFocusSessions() {
        if (_focusSessions.value > 0) {
            _focusSessions.value--
            setEndTime(
                calculateFromFocusSessions(
                    focusSessions = focusSessions.value,
                    sessionTime = sessionTime.value ?: 25,
                    shortBreakTime = shortBreakTime.value ?: 5,
                    longBreakTime = longBreakTime.value ?: 15,
                    currentLocalDateTime = LocalDateTime(
                        year = taskDate.value.year,
                        month = taskDate.value.month,
                        dayOfMonth = taskDate.value.dayOfMonth,
                        hour = startTime.value.hour,
                        minute = startTime.value.minute,
                    ),
                ),
            )
        }
    }

    private fun setFocusSessions(sessions: Int) {
        _focusSessions.value = sessions
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

    private val _taskDate = MutableStateFlow(today())
    val taskDate = _taskDate.asStateFlow()
    fun setTaskDate(date: LocalDateTime) {
        _taskDate.value = date
    }

    private val _startTime = MutableStateFlow(today().time)
    val startTime = _startTime.asStateFlow()
    fun setStartTime(time: LocalTime) {
        _startTime.value = time
    }

    private val _endTime = MutableStateFlow(today().time)
    val endTime = _endTime.asStateFlow()
    fun setEndTime(time: LocalTime) {
        _endTime.value = time
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

    fun addTask(task: Task) {
        screenModelScope.launch {
            tasksRepository.addTask(task)
            reset()
            setEndTime(today().time)
            _eventsFlow.trySend(UiEvents.ShowSnackbar("Task added!"))
        }
    }

    private fun reset() {
        setFocusSessions(1)
        setTaskName("")
        setTaskDescription("")
        setSelectedOption(taskTypes.last())
        setTaskDate(today())
        setStartTime(today().time)
        // setEndTime(today().time)
        setTask(null)
        _showStartTimeInputDialog.value = false
    }

    private fun setTask(task: Task?) {
        _task.value = task
    }

    private val _task = MutableStateFlow<Task?>(null)
    val task = _task.asStateFlow()
    fun getTask(taskId: Int?) {
        screenModelScope.launch {
            if (taskId != null) {
                tasksRepository.getTask(taskId).collectLatest {
                    _task.value = it
                    prefillFields(it)
                }
            } else {
                reset()
            }
        }
    }

    private fun prefillFields(it: Task?) {
        setTaskName(it?.name ?: "")
        setTaskDescription(it?.description ?: "")
        setSelectedOption(
            taskTypes.firstOrNull { taskType ->
                taskType.name == it?.type
            } ?: taskTypes.last(),
        )
        setFocusSessions(it?.focusSessions ?: 1)
        setTaskDate(it?.date ?: today())
        setStartTime(it?.start?.time ?: today().time)
        setEndTime(
            calculateFromFocusSessions(
                focusSessions = it?.focusSessions ?: 1,
                sessionTime = sessionTime.value ?: 25,
                shortBreakTime = shortBreakTime.value ?: 5,
                longBreakTime = longBreakTime.value ?: 15,
                currentLocalDateTime = LocalDateTime(
                    year = it?.date?.year ?: today().year,
                    month = it?.date?.month ?: today().month,
                    dayOfMonth = it?.date?.dayOfMonth ?: today().dayOfMonth,
                    hour = it?.start?.time?.hour ?: today().time.hour,
                    minute = it?.start?.time?.minute ?: today().time.minute,
                ),
            ),
        )
    }

    fun updateTask(task: Task) {
        screenModelScope.launch {
            tasksRepository.updateTask(task)
            reset()
            setEndTime(today().time)
            _eventsFlow.trySend(UiEvents.NavigateBack)
        }
    }
}
