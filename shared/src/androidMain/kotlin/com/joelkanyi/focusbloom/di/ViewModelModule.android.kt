/*
 * Copyright 2024 Joel Kanyi.
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

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.joelkanyi.focusbloom.feature.home.HomeViewModel
import com.joelkanyi.focusbloom.feature.addtask.AddTaskViewModel
import com.joelkanyi.focusbloom.feature.calendar.CalendarViewModel
import com.joelkanyi.focusbloom.feature.onboarding.OnboardingViewModel
import com.joelkanyi.focusbloom.feature.settings.SettingsViewModel
import com.joelkanyi.focusbloom.feature.statistics.StatisticsViewModel
import com.joelkanyi.focusbloom.feature.taskprogress.TaskProgressViewModel
import com.joelkanyi.focusbloom.main.MainViewModel

actual val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::AddTaskViewModel)
    viewModelOf(::CalendarViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::StatisticsViewModel)
    viewModelOf(::TaskProgressViewModel)
    viewModelOf(::MainViewModel)
}
