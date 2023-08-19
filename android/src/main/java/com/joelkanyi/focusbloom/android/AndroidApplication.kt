package com.joelkanyi.focusbloom.android

import android.app.Application
import com.joelkanyi.focusbloom.android.di.androidModule
import com.joelkanyi.focusbloom.di.calendarModule
import com.joelkanyi.focusbloom.di.homeModule
import com.joelkanyi.focusbloom.di.initKoin
import com.joelkanyi.focusbloom.di.settingsModule
import com.joelkanyi.focusbloom.di.statisticsModule
import io.github.aakira.napier.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(level = if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(androidContext = this@AndroidApplication)
            modules(
                listOf(
                    androidModule,
                    homeModule,
                    settingsModule,
                    calendarModule,
                    statisticsModule,
                ),
            )
        }
    }
}
