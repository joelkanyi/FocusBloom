package com.joelkanyi.focusbloom.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HomeScreenModel(
    private val tasksRepository: TasksRepository,
) : ScreenModel {
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
