package com.joelkanyi.focusbloom.feature.settings

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsScreenModel(
    private val settingsRepository: SettingsRepository,
) : ScreenModel {
    private val _selectedColorCardTitle = MutableStateFlow("")
    val selectedColorCardTitle = _selectedColorCardTitle.asStateFlow()
    fun setSelectedColorCardTitle(title: String) {
        _selectedColorCardTitle.value = title
    }

    val hourFormats: List<String> = listOf("12-hour", "24-hour")

    val optionsOpened = mutableStateListOf("")
    fun openOptions(option: String) {
        if (optionsOpened.contains(option)) {
            optionsOpened.remove(option)
        } else {
            optionsOpened.add(option)
        }
    }

    val appTheme: StateFlow<Int?> = settingsRepository.getAppTheme()
        .map { it }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    fun setAppTheme(appTheme: Int) {
        coroutineScope.launch {
            settingsRepository.saveAppTheme(appTheme)
        }
    }

    val sessionTime = settingsRepository.getSessionTime()
        .map {
            it
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    fun setSessionTime(sessionTime: Int) {
        coroutineScope.launch {
            settingsRepository.saveSessionTime(sessionTime)
        }
    }

    val shortBreakTime = settingsRepository.getShortBreakTime()
        .map { it }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    fun setShortBreakTime(shortBreakTime: Int) {
        coroutineScope.launch {
            settingsRepository.saveShortBreakTime(shortBreakTime)
        }
    }

    val longBreakTime = settingsRepository.getLongBreakTime()
        .map { it }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    fun setLongBreakTime(longBreakTime: Int) {
        coroutineScope.launch {
            settingsRepository.saveLongBreakTime(longBreakTime)
        }
    }

    val timeFormat = settingsRepository.getHourFormat()
        .map { it }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    fun setHourFormat(timeFormat: Int) {
        coroutineScope.launch {
            settingsRepository.saveHourFormat(timeFormat)
        }
    }

    fun setShortBreakColor(color: Long) {
        coroutineScope.launch {
            settingsRepository.saveShortBreakColor(color)
        }
    }

    fun setLongBreakColor(color: Long) {
        coroutineScope.launch {
            settingsRepository.saveLongBreakColor(color)
        }
    }

    fun setFocusColor(color: Long) {
        coroutineScope.launch {
            settingsRepository.saveFocusColor(color)
        }
    }

    private val _showColorDialog = MutableStateFlow(false)
    val showColorDialog = _showColorDialog.asStateFlow()
    fun setShowColorDialog(it: Boolean) {
        _showColorDialog.value = it
    }

    val shortBreakColor = settingsRepository.shortBreakColor()
        .map { it }
        .stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val longBreakColor = settingsRepository.longBreakColor()
        .map { it }
        .stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val focusColor = settingsRepository.focusColor()
        .map { it }
        .stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )
}
