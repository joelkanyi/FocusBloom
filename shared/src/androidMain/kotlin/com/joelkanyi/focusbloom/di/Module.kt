package com.joelkanyi.focusbloom.di

import com.joelkanyi.focusbloom.platform.MultiplatformSettingsWrapper
import com.russhwolf.settings.ExperimentalSettingsApi
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
actual fun platformModule(): Module = module {
    single { MultiplatformSettingsWrapper(context = get()).createSettings() }
}
