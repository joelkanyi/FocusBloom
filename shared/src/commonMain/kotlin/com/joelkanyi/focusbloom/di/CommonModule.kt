/*
 * Copyright 2023 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joelkanyi.focusbloom.di

import com.joelkanyi.focusbloom.core.data.local.setting.PreferenceManager
import com.joelkanyi.focusbloom.core.data.repository.settings.SettingsRepositoryImpl
import com.joelkanyi.focusbloom.core.data.repository.tasks.TasksRepositoryImpl
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import com.joelkanyi.focusbloom.database.BloomDatabase
import com.joelkanyi.focusbloom.feature.addtask.AddTaskScreenModel
import com.joelkanyi.focusbloom.feature.calendar.CalendarScreenModel
import com.joelkanyi.focusbloom.feature.home.HomeScreenModel
import com.joelkanyi.focusbloom.feature.onboarding.OnboadingViewModel
import com.joelkanyi.focusbloom.feature.settings.SettingsScreenModel
import com.joelkanyi.focusbloom.feature.statistics.StatisticsScreenModel
import com.joelkanyi.focusbloom.feature.taskprogress.TaskProgressScreenModel
import com.joelkanyi.focusbloom.main.MainViewModel
import com.joelkanyi.focusbloom.platform.DatabaseDriverFactory
import com.russhwolf.settings.ExperimentalSettingsApi
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
fun commonModule() = module {
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
            settingsRepository = get(),
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
            settingsRepository = get(),
        )
    }

    single<TaskProgressScreenModel> {
        TaskProgressScreenModel(
            settingsRepository = get(),
            tasksRepository = get(),
        )
    }

    single<MainViewModel> {
        MainViewModel(
            settingsRepository = get(),
        )
    }

    single<OnboadingViewModel> {
        OnboadingViewModel(
            settingsRepository = get(),
        )
    }
}

expect fun platformModule(): Module
