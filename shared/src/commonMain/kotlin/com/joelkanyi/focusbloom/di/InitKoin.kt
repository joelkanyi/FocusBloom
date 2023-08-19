package com.joelkanyi.focusbloom.di

import org.koin.core.Koin
import org.koin.core.context.startKoin

class InitKoin {
    operator fun invoke(): Koin {
        return startKoin {
            modules(
                listOf(
                    commonModule(true),
                    homeModule,
                    settingsModule,
                    calendarModule,
                    statisticsModule,
                ),
            )
        }.koin
    }
}
