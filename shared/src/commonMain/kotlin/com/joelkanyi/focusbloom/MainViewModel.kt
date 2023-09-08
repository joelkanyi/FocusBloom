package com.joelkanyi.focusbloom

import cafe.adriel.voyager.core.model.ScreenModel
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository

class MainViewModel(
    private val settingsRepository: SettingsRepository,
) : ScreenModel {

    val appTheme = settingsRepository.getAppTheme()
}
