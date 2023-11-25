package com.joelkanyi.focusbloom.platform

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.coroutines.toSuspendSettings

actual class MultiplatformSettingsWrapper {
    @OptIn(ExperimentalSettingsApi::class)
    actual fun createSettings(): SuspendSettings {
        return StorageSettings().toSuspendSettings()
    }
}
