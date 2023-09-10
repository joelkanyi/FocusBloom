package com.joelkanyi.focusbloom.task

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AddTaskScreenModel(
    private val settingsRepository: SettingsRepository,
    private val tasksRepository: TasksRepository,
) : ScreenModel {
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
    val timeFormat = settingsRepository.getTimeFormat()
        .map { it ?: 0 }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0,
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
}
