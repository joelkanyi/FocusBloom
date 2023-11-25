package com.joelkanyi.focusbloom.di

import com.joelkanyi.focusbloom.platform.DatabaseDriverFactory
import com.joelkanyi.focusbloom.platform.MultiplatformSettingsWrapper
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory() }
    single { MultiplatformSettingsWrapper().createSettings() }
}
