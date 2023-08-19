package com.joelkanyi.focusbloom.di

import com.joelkanyi.focusbloom.Statistics
import com.joelkanyi.focusbloom.presentation.StatisticsViewModel
import org.koin.dsl.module

val statisticsModule = module {
    single { Statistics() }
    single { StatisticsViewModel(statistics = get()) }
}
