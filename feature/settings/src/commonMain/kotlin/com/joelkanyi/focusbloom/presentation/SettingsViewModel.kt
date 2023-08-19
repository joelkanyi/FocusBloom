package com.joelkanyi.focusbloom.presentation

import com.joelkanyi.focusbloom.Settings
import org.koin.core.component.KoinComponent

class SettingsViewModel(
    private val settings: Settings,
) : KoinComponent {
    fun getSettings() = settings.getSettings()
}
