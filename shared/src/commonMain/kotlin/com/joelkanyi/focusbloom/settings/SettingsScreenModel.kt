package com.joelkanyi.focusbloom.settings

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsScreenModel(
    private val settingsRepository: SettingsRepository,
) : ScreenModel {

    val appTheme = settingsRepository.getAppTheme()

    val optionsOpened = mutableStateListOf("")
    fun openOptions(option: String) {
        if (optionsOpened.contains(option)) {
            optionsOpened.remove(option)
        } else {
            optionsOpened.add(option)
        }
    }

    fun setAppTheme(appTheme: Int) {
        coroutineScope.launch {
            settingsRepository.saveAppTheme(appTheme)
        }
    }

    val sessionTime = settingsRepository.getSessionTime()
        .map {
            it ?: 25
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 25,
        )

    fun setSessionTime(sessionTime: Int) {
        coroutineScope.launch {
            settingsRepository.saveSessionTime(sessionTime)
        }
    }

    val shortBreakTime = settingsRepository.getShortBreakTime()
        .map { it ?: 5 }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 5,
        )

    fun setShortBreakTime(shortBreakTime: Int) {
        coroutineScope.launch {
            settingsRepository.saveShortBreakTime(shortBreakTime)
        }
    }

    val longBreakTime = settingsRepository.getLongBreakTime()
        .map { it ?: 15 }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 15,
        )

    fun setLongBreakTime(longBreakTime: Int) {
        coroutineScope.launch {
            settingsRepository.saveLongBreakTime(longBreakTime)
        }
    }

    val timeFormat = settingsRepository.getTimeFormat()
        .map { it ?: 0 }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0,
        )

    fun setTimeFormat(timeFormat: Int) {
        coroutineScope.launch {
            settingsRepository.saveTimeFormat(timeFormat)
        }
    }
}
