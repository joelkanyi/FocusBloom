package ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import com.joelkanyi.focusbloom.presentation.App
import com.joelkanyi.focusbloom.presentation.CalendarViewModel
import com.joelkanyi.focusbloom.presentation.HomeViewModel
import com.joelkanyi.focusbloom.presentation.SettingsViewModel
import com.joelkanyi.focusbloom.presentation.StatisticsViewModel
import koin

@Composable
fun MainScreen(
    applicationScope: ApplicationScope,
    homeViewModel: HomeViewModel = koin.get(),
    calendarViewModel: CalendarViewModel = koin.get(),
    settingsViewModel: SettingsViewModel = koin.get(),
    statisticsViewModel: StatisticsViewModel = koin.get(),
) {
    // val greeting = viewModel.greeting.collectAsState().value

    Window(
        onCloseRequest = { applicationScope.exitApplication() },
        title = "KMP Template",
        state = rememberWindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            width = 400.dp, // or Dp.Unspecified,
            height = 400.dp, // or Dp.Unspecified,
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            App()
            /*Column {
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
            }*/
        }
    }
}
