package com.joelkanyi.focusbloom.settings

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import kotlinx.coroutines.launch

class SettingsScreenModel(
    private val settingsRepository: SettingsRepository,
) : ScreenModel {

    val appTheme = settingsRepository.getAppTheme()

    fun setAppTheme(appTheme: Int) {
        coroutineScope.launch {
            settingsRepository.saveAppTheme(appTheme)
        }
    }
}
