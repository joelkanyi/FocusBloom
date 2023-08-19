package com.joelkanyi.focusbloom.di

import com.joelkanyi.focusbloom.Settings
import com.joelkanyi.focusbloom.presentation.SettingsViewModel
import org.koin.dsl.module

val settingsModule = module {
    single { Settings() }
    single { SettingsViewModel(settings = get()) }
}
