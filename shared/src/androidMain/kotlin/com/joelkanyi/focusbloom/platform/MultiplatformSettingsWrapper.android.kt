package com.joelkanyi.focusbloom.platform

import android.content.Context
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings

actual class MultiplatformSettingsWrapper(private val context: Context) {
    @OptIn(ExperimentalSettingsApi::class)
    actual fun createSettings(): ObservableSettings {
        val sharedPreferences =
            context.getSharedPreferences("bloom_preferences", Context.MODE_PRIVATE)
        return AndroidSettings(delegate = sharedPreferences)
    }
}