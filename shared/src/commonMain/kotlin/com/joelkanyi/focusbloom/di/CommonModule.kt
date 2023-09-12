package com.joelkanyi.focusbloom.di

import com.joelkanyi.focusbloom.calendar.CalendarScreenModel
import com.joelkanyi.focusbloom.core.data.local.setting.PreferenceManager
import com.joelkanyi.focusbloom.core.data.repository.settings.SettingsRepositoryImpl
import com.joelkanyi.focusbloom.core.data.repository.tasks.TasksRepositoryImpl
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import com.joelkanyi.focusbloom.database.BloomDatabase
import com.joelkanyi.focusbloom.home.HomeScreenModel
import com.joelkanyi.focusbloom.platform.DatabaseDriverFactory
import com.joelkanyi.focusbloom.settings.SettingsScreenModel
import com.joelkanyi.focusbloom.statistics.StatisticsScreenModel
import com.joelkanyi.focusbloom.task.AddTaskScreenModel
import com.russhwolf.settings.ExperimentalSettingsApi
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
fun commonModule(isDebug: Boolean) = module {
    /**
     * Database
     */
    single<BloomDatabase> {
        BloomDatabase(
            driver = get<DatabaseDriverFactory>().createDriver(),
        )
    }
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
            bloomDatabase = get(),
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
    single<HomeScreenModel> {
        HomeScreenModel(
            tasksRepository = get(),
        )
    }
    single<StatisticsScreenModel> {
        StatisticsScreenModel(
            tasksRepository = get(),
            settingsRepository = get(),
        )
    }
    single<CalendarScreenModel> {
        CalendarScreenModel(
            tasksRepository = get(),
        )
    }
}

expect fun platformModule(): Module
