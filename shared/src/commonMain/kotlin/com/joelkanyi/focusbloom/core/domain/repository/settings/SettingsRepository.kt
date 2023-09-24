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
}
