package com.joelkanyi.focusbloom.android.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.joelkanyi.focusbloom.presentation.CalendarViewModel
import com.joelkanyi.focusbloom.presentation.HomeViewModel
import com.joelkanyi.focusbloom.presentation.SettingsViewModel
import com.joelkanyi.focusbloom.presentation.StatisticsViewModel
import org.koin.androidx.compose.get
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    homeViewModel: HomeViewModel = koinInject(),
    calendarViewModel: CalendarViewModel = koinInject(),
    settingsViewModel: SettingsViewModel = koinInject(),
    statisticsViewModel: StatisticsViewModel = koinInject(),
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(
                text = "Settings: ${settingsViewModel.getSettings()}",
                color = Color.Black,
            )
            Text(
                text = "Calendar: ${calendarViewModel.getCalendar()}",
                color = Color.Black,
            )
            Text(
                text = "Home: ${homeViewModel.getHome()}",
                color = Color.Black,
            )
            Text(
                text = "Statistics: ${statisticsViewModel.getStatistics()}",
                color = Color.Black,
            )
        }
    }
}
