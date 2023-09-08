package com.joelkanyi.focusbloom.core.data.repository.settings

import com.joelkanyi.focusbloom.core.data.local.setting.PreferenceManager
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl constructor(
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

    override fun getTimeFormat(): Flow<Int?> {
        return preferenceManager.getInt(key = PreferenceManager.TIME_FORMAT)
    }
}
