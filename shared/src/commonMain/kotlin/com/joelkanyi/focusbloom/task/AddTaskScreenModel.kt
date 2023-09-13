package com.joelkanyi.focusbloom.task

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
            it ?: 25
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 25,
        )
    val shortBreakTime = settingsRepository.getShortBreakTime()
        .map { it ?: 5 }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 5,
        )
    val longBreakTime = settingsRepository.getLongBreakTime()
        .map { it ?: 15 }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 15,
        )
    val hourFormat = settingsRepository.getHourFormat()
        .map { it ?: 24 }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 24,
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

    private val _taskName = MutableStateFlow("")
    val taskName = _taskName.asStateFlow()
    fun setTaskName(name: String) {
        _taskName.value = name
    }

    private val _taskDescription = MutableStateFlow("")
    val taskDescription = _taskDescription.asStateFlow()
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
