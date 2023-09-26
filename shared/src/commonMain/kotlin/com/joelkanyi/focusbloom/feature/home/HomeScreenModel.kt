package com.joelkanyi.focusbloom.feature.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import com.joelkanyi.focusbloom.core.utils.plusDays
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
    settingsRepository: SettingsRepository,
) : ScreenModel {
    private val _openBottomSheet = MutableStateFlow(false)
    val openBottomSheet = _openBottomSheet.asStateFlow()
    fun openBottomSheet(value: Boolean) {
        _openBottomSheet.value = value
    }

    val hourFormat = settingsRepository.getHourFormat()
        .map { it ?: 24 }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 24,
        )

    fun deleteTask(task: Task) {
        coroutineScope.launch {
            tasksRepository.deleteTask(task.id)
        }
    }

    fun updateTask(task: Task) {
        coroutineScope.launch {
            tasksRepository.updateTask(task)
        }
    }

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask = _selectedTask.asStateFlow()
    fun selectTask(task: Task?) {
        _selectedTask.value = task
    }

    fun pushToTomorrow(task: Task) {
        coroutineScope.launch {
            tasksRepository.updateTask(
                task.copy(
                    date = task.date.plusDays(1),
                    start = task.start.plusDays(1),
                    end = task.end.plusDays(1),
                ),
            )
        }
    }

    fun markAsCompleted(task: Task) {
        coroutineScope.launch {
            tasksRepository.updateTaskCompleted(
                id = task.id,
                completed = true,
            )
        }
    }

    val tasks = tasksRepository.getTasks()
        .map { tasks ->
            TasksState.Success(
                tasks
                    .sortedBy { it.start }
                    .filter {
                        it.date.date == Clock.System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date/*.minusDays(1)*/
                    },
            )
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TasksState.Loading,
        )

    val username = settingsRepository.getUsername()
        .map { it }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
}

sealed class TasksState {
    data object Loading : TasksState()
    data class Success(val tasks: List<Task>) : TasksState()
}
