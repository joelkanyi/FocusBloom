package com.joelkanyi.focusbloom.statistics

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StatisticsScreenModel(
    tasksRepository: TasksRepository,
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
            tasks
                .sortedByDescending { it.date }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )
}
