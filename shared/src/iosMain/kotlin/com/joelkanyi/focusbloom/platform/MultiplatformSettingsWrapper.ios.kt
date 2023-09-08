package com.joelkanyi.focusbloom.platform

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import platform.Foundation.NSUserDefaults

actual class MultiplatformSettingsWrapper {
    @OptIn(ExperimentalSettingsApi::class)
    actual fun createSettings(): ObservableSettings {
        val nsUserDefault = NSUserDefaults.standardUserDefaults
        return AppleSettings(delegate = nsUserDefault)
    }
}