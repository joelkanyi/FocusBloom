package com.joelkanyi.focusbloom.home

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HomeScreenModel(
    private val tasksRepository: TasksRepository,
) : ScreenModel {
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

    val tasks = tasksRepository.getTasks()
        .map { tasks ->
            tasks
                .sortedBy { it.start }
                .filter {
                    it.date.date == Clock.System.now()
                        .toLocalDateTime(TimeZone.currentSystemDefault()).date
                }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )
}
