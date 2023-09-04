package com.joelkanyi.focusbloom.android

import android.app.Application
import com.joelkanyi.focusbloom.android.di.androidModule
import com.joelkanyi.focusbloom.di.initKoin
import io.github.aakira.napier.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class FocusBloomApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(level = if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(androidContext = this@FocusBloomApp)
            modules(
                listOf(
                    androidModule,
                ),
            )
        }
    }
}
