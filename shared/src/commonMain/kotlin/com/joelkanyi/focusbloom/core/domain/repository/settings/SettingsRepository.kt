package com.joelkanyi.focusbloom.core.domain.repository.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun saveAppTheme(theme: Int)
    fun getAppTheme(): Flow<Int?>
    fun clearAll()
    fun getSessionTime(): Flow<Int?>
    fun getShortBreakTime(): Flow<Int?>
    fun getLongBreakTime(): Flow<Int?>
    fun getTimeFormat(): Flow<Int?>
    fun saveSessionTime(sessionTime: Int)
    fun saveLongBreakTime(longBreakTime: Int)
    fun saveTimeFormat(timeFormat: Int)
    fun saveShortBreakTime(shortBreakTime: Int)
}
