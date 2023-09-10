package com.joelkanyi.focusbloom.di

import com.joelkanyi.focusbloom.core.data.local.setting.PreferenceManager
import com.joelkanyi.focusbloom.core.data.repository.settings.SettingsRepositoryImpl
import com.joelkanyi.focusbloom.core.data.repository.tasks.TasksRepositoryImpl
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import com.joelkanyi.focusbloom.settings.SettingsScreenModel
import com.joelkanyi.focusbloom.task.AddTaskScreenModel
import com.russhwolf.settings.ExperimentalSettingsApi
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
fun commonModule(isDebug: Boolean) = module {
    /**
     * Multiplatform-Settings
     */
    single<PreferenceManager> {
        PreferenceManager(observableSettings = get())
    }

    /**
     * Repositories
     */
    single<SettingsRepository> {
        SettingsRepositoryImpl(
            preferenceManager = get(),
        )
    }

    single<TasksRepository> {
        TasksRepositoryImpl(
            databaseDriverFactory = get(),
        )
    }

    /**
     * ViewModels
     */
    single<SettingsScreenModel> {
        SettingsScreenModel(
            settingsRepository = get(),
        )
    }
    single<AddTaskScreenModel> {
        AddTaskScreenModel(
            settingsRepository = get(),
            tasksRepository = get(),
        )
    }
}

expect fun platformModule(): Module
