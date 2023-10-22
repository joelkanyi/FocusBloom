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
package com.joelkanyi.focusbloom.core.domain.repository.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun saveAppTheme(theme: Int)
    fun getAppTheme(): Flow<Int?>
    fun clearAll()
    fun getSessionTime(): Flow<Int?>
    fun getShortBreakTime(): Flow<Int?>
    fun getLongBreakTime(): Flow<Int?>
    fun getHourFormat(): Flow<Int?>
    fun saveSessionTime(sessionTime: Int)
    fun saveLongBreakTime(longBreakTime: Int)
    fun saveHourFormat(timeFormat: Int)
    fun saveShortBreakTime(shortBreakTime: Int)
    fun shortBreakColor(): Flow<Long?>
    fun saveShortBreakColor(color: Long)
    fun longBreakColor(): Flow<Long?>
    fun saveLongBreakColor(color: Long)
    fun focusColor(): Flow<Long?>
    fun saveFocusColor(color: Long)
    fun saveUsername(value: String)
    fun getUsername(): Flow<String?>
    fun remindersOn(): Flow<Int?>
    fun toggleReminder(value: Int)
}
