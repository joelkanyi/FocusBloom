/*
 * Copyright 2026 Joel Kanyi.
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
package com.joelkanyi.focusbloom.core.datastore

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.russhwolf.settings.coroutines.getIntFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeMode { SYSTEM, LIGHT, DARK }

/**
 * Typed, observable app preferences over a platform [ObservableSettings]. Each app supplies the
 * concrete store (Android SharedPreferences, iOS NSUserDefaults, JVM Preferences, Web storage);
 * this class exposes reads as [Flow]s and writes as plain setters.
 */
@OptIn(ExperimentalSettingsApi::class)
class FocusBloomSettings(private val settings: ObservableSettings) {

    fun themeMode(): Flow<ThemeMode> =
        settings.getStringOrNullFlow(KEY_THEME).map { stored ->
            stored?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() } ?: ThemeMode.SYSTEM
        }

    fun setThemeMode(mode: ThemeMode) = settings.putString(KEY_THEME, mode.name)

    fun hourFormat(): Flow<Int> = settings.getIntFlow(KEY_HOUR_FORMAT, DEFAULT_HOUR_FORMAT)
    fun setHourFormat(hours: Int) = settings.putInt(KEY_HOUR_FORMAT, hours)

    fun focusMinutes(): Flow<Int> = settings.getIntFlow(KEY_FOCUS_MIN, DEFAULT_FOCUS_MIN)
    fun setFocusMinutes(minutes: Int) = settings.putInt(KEY_FOCUS_MIN, minutes)

    fun shortBreakMinutes(): Flow<Int> = settings.getIntFlow(KEY_SHORT_MIN, DEFAULT_SHORT_MIN)
    fun setShortBreakMinutes(minutes: Int) = settings.putInt(KEY_SHORT_MIN, minutes)

    fun longBreakMinutes(): Flow<Int> = settings.getIntFlow(KEY_LONG_MIN, DEFAULT_LONG_MIN)
    fun setLongBreakMinutes(minutes: Int) = settings.putInt(KEY_LONG_MIN, minutes)

    fun onboarded(): Flow<Boolean> = settings.getBooleanFlow(KEY_ONBOARDED, false)
    fun setOnboarded(value: Boolean) = settings.putBoolean(KEY_ONBOARDED, value)

    private companion object {
        const val KEY_THEME = "theme_mode"
        const val KEY_HOUR_FORMAT = "hour_format"
        const val KEY_FOCUS_MIN = "focus_minutes"
        const val KEY_SHORT_MIN = "short_break_minutes"
        const val KEY_LONG_MIN = "long_break_minutes"
        const val KEY_ONBOARDED = "onboarded"

        const val DEFAULT_HOUR_FORMAT = 24
        const val DEFAULT_FOCUS_MIN = 25
        const val DEFAULT_SHORT_MIN = 5
        const val DEFAULT_LONG_MIN = 15
    }
}
