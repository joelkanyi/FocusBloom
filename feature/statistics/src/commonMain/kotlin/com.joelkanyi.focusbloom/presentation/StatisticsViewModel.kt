package com.joelkanyi.focusbloom.presentation

import com.joelkanyi.focusbloom.Statistics
import org.koin.core.component.KoinComponent

class StatisticsViewModel(
    private val statistics: Statistics,
) : KoinComponent {
    fun getStatistics() = statistics.getStatistics()
}
