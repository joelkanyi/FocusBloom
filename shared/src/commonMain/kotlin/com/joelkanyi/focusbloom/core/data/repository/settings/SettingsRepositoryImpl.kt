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
package com.joelkanyi.focusbloom.core.data.repository.settings

import com.joelkanyi.focusbloom.core.data.local.setting.PreferenceManager
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val preferenceManager: PreferenceManager,
) : SettingsRepository {
    override suspend fun saveAppTheme(theme: Int) {
        preferenceManager.setInt(key = PreferenceManager.APP_THEME, value = theme)
    }

    override fun getAppTheme(): Flow<Int?> {
        return preferenceManager.getInt(key = PreferenceManager.APP_THEME)
    }

    override fun clearAll() {
        return preferenceManager.clearPreferences()
    }

    override fun getSessionTime(): Flow<Int?> {
        return preferenceManager.getInt(key = PreferenceManager.FOCUS_TIME)
    }

    override fun getShortBreakTime(): Flow<Int?> {
        return preferenceManager.getInt(key = PreferenceManager.SHORT_BREAK_TIME)
    }

    override fun getLongBreakTime(): Flow<Int?> {
        return preferenceManager.getInt(key = PreferenceManager.LONG_BREAK_TIME)
    }

    override fun getHourFormat(): Flow<Int?> {
        return preferenceManager.getInt(key = PreferenceManager.HOUR_FORMAT)
    }

    override fun saveSessionTime(sessionTime: Int) {
        preferenceManager.setInt(key = PreferenceManager.FOCUS_TIME, value = sessionTime)
    }

    override fun saveLongBreakTime(longBreakTime: Int) {
        preferenceManager.setInt(key = PreferenceManager.LONG_BREAK_TIME, value = longBreakTime)
    }

    override fun saveHourFormat(timeFormat: Int) {
        preferenceManager.setInt(key = PreferenceManager.HOUR_FORMAT, value = timeFormat)
    }

    override fun saveShortBreakTime(shortBreakTime: Int) {
        preferenceManager.setInt(key = PreferenceManager.SHORT_BREAK_TIME, value = shortBreakTime)
    }

    override fun shortBreakColor(): Flow<Long?> {
        return preferenceManager.getLong(key = PreferenceManager.SHORT_BREAK_COLOR)
    }

    override fun saveShortBreakColor(color: Long) {
        preferenceManager.setLong(key = PreferenceManager.SHORT_BREAK_COLOR, value = color)
    }

    override fun longBreakColor(): Flow<Long?> {
        return preferenceManager.getLong(key = PreferenceManager.LONG_BREAK_COLOR)
    }

    override fun saveLongBreakColor(color: Long) {
        preferenceManager.setLong(key = PreferenceManager.LONG_BREAK_COLOR, value = color)
    }

    override fun focusColor(): Flow<Long?> {
        return preferenceManager.getLong(key = PreferenceManager.FOCUS_COLOR)
    }

    override fun saveFocusColor(color: Long) {
        preferenceManager.setLong(key = PreferenceManager.FOCUS_COLOR, value = color)
    }

    override fun saveUsername(value: String) {
        preferenceManager.setString(key = PreferenceManager.USERNAME, value = value)
    }

    override fun getUsername(): Flow<String?> {
        return preferenceManager.getString(key = PreferenceManager.USERNAME)
    }

    override fun remindersOn(): Flow<Int?> {
        return preferenceManager.getInt(key = PreferenceManager.NOTIFICATION_OPTION)
    }

    override fun toggleReminder(value: Int) {
        preferenceManager.setInt(key = PreferenceManager.NOTIFICATION_OPTION, value = value)
    }
}
