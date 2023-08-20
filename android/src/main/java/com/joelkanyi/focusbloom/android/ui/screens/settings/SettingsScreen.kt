package com.joelkanyi.focusbloom.android.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.joelkanyi.focusbloom.presentation.CalendarViewModel
import com.joelkanyi.focusbloom.presentation.HomeViewModel
import com.joelkanyi.focusbloom.presentation.SettingsViewModel
import com.joelkanyi.focusbloom.presentation.StatisticsViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.compose.koinInject

@Destination
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator,
    homeViewModel: HomeViewModel = koinInject(),
    calendarViewModel: CalendarViewModel = koinInject(),
    settingsViewModel: SettingsViewModel = koinInject(),
    statisticsViewModel: StatisticsViewModel = koinInject(),
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            ) {
                Column(
                    Modifier.padding(16.dp),
                ) {
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
    }
}
