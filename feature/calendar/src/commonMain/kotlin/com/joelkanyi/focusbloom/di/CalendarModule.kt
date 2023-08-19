package com.joelkanyi.focusbloom.di

import com.joelkanyi.focusbloom.Calendar
import com.joelkanyi.focusbloom.presentation.CalendarViewModel
import org.koin.dsl.module

val calendarModule = module {
    single { Calendar() }
    single { CalendarViewModel(calendar = get()) }
}
