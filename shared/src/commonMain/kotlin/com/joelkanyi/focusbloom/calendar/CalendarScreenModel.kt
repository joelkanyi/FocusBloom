package com.joelkanyi.focusbloom.calendar

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CalendarScreenModel(
    private val tasksRepository: TasksRepository,
) : ScreenModel {
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
}
