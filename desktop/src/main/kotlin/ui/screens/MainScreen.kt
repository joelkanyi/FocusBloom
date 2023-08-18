package ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import com.joelkanyi.focusbloom.presentation.viewmodels.MainViewModel
import koin

@Composable
fun MainScreen(applicationScope: ApplicationScope, viewModel: MainViewModel = koin.get()) {
    val greeting = viewModel.greeting.collectAsState().value

    Window(
        onCloseRequest = { applicationScope.exitApplication() },
        title = "KMP Template",
        state = rememberWindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            width = 400.dp, // or Dp.Unspecified,
            height = 400.dp, // or Dp.Unspecified,
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            greeting?.let {
                Text(modifier = Modifier.align(Alignment.Center), text = it)
            }
        }
    }
}
