package com.joelkanyi.focusbloom.statistics

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StatisticsScreenModel(
    private val tasksRepository: TasksRepository,
    settingsRepository: SettingsRepository,
) : ScreenModel {
    val hourFormat = settingsRepository.getHourFormat()
        .map {
            it ?: 24
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 24,
        )
    val tasks = tasksRepository.getTasks()
        .map { tasks ->
            tasks.sortedByDescending { it.date }
                .filter {
                    it.completed
                }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
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

    val openedTasks = mutableStateListOf<Task?>(null)
    fun openTaskOptions(task: Task?) {
        if (openedTasks.contains(task)) {
            openedTasks.remove(task)
        } else {
            openedTasks.add(task)
        }
    }
}
