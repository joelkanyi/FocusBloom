package com.joelkanyi.focusbloom.platform

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings

expect class MultiplatformSettingsWrapper {
    @OptIn(ExperimentalSettingsApi::class)
    fun createSettings(): ObservableSettings
}