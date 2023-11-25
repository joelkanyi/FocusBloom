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
package com.joelkanyi.focusbloom.core.data.local.setting

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.russhwolf.settings.coroutines.getIntFlow
import com.russhwolf.settings.coroutines.getIntOrNullFlow
import com.russhwolf.settings.coroutines.getLongFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import kotlinx.coroutines.flow.Flow

class PreferenceManager(private val settings: SuspendSettings) {
    private val observableSettings: ObservableSettings by lazy { settings as ObservableSettings }

    @OptIn(ExperimentalSettingsApi::class)
    private val suspendSettings = settings

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun setString(key: String, value: String) {
        // observableSettings.set(key = key, value = value)
        suspendSettings.putString(key = key, value = value)
    }

    fun getNonFlowString(key: String) = observableSettings.getString(
        key = key,
        defaultValue = "",
    )

    @OptIn(ExperimentalSettingsApi::class)
    fun getString(key: String) = observableSettings.getStringOrNullFlow(key = key)

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun setInt(key: String, value: Int) {
        // observableSettings.set(key = key, value = value)
        suspendSettings.putInt(key = key, value = value)
    }

    @OptIn(ExperimentalSettingsApi::class)
    fun getInt(key: String) = observableSettings.getIntOrNullFlow(key = key)

    @OptIn(ExperimentalSettingsApi::class)
    fun getIntFlow(key: String) = observableSettings.getIntFlow(key = key, defaultValue = 0)

    companion object {
        const val NOTIFICATION_OPTION = "notification_option_key"
        const val USERNAME = "username_key"
        const val SHORT_BREAK_COLOR = "short_break_color_key"
        const val LONG_BREAK_COLOR = "long_break_color_key"
        const val FOCUS_COLOR = "focus_color_key"
        const val APP_THEME = "app_theme_key"
        const val FOCUS_TIME = "focus_time_key"
        const val SHORT_BREAK_TIME = "short_break_time_key"
        const val LONG_BREAK_TIME = "long_break_time_key"
        const val HOUR_FORMAT = "hour_format_key"
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun clearPreferences() {
        // observableSettings.clear()
        suspendSettings.clear()
    }

    @OptIn(ExperimentalSettingsApi::class)
    fun getBoolean(key: String): Flow<Boolean> {
        return observableSettings.getBooleanFlow(
            key = key,
            defaultValue = false,
        )
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun setBoolean(key: String, value: Boolean) {
        // observableSettings.set(key = key, value = value)
        suspendSettings.putBoolean(key = key, value = value)
    }

    @OptIn(ExperimentalSettingsApi::class)
    fun getLong(key: Any): Flow<Long?> {
        return observableSettings.getLongFlow(
            key = key.toString(),
            defaultValue = 0,
        )
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun setLong(key: String, value: Long) {
        // observableSettings.set(key = key, value = value)
        suspendSettings.putLong(key = key, value = value)
    }
}
