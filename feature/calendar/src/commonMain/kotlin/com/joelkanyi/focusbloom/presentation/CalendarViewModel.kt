package com.joelkanyi.focusbloom.presentation

import com.joelkanyi.focusbloom.Calendar
import org.koin.core.component.KoinComponent

class CalendarViewModel(
    private val calendar: Calendar,
) : KoinComponent {
    fun getCalendar() = calendar.getCalendar()
}
