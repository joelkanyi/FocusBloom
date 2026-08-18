package com.joelkanyi.focusbloom.shell

import androidx.compose.runtime.Composable
import com.joelkanyi.focusbloom.core.designsystem.theme.FocusBloomTheme
import com.joelkanyi.focusbloom.feature.focus.FocusScreen
import com.joelkanyi.focusbloom.feature.focus.FocusViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * The shared application root, rendered identically by every platform launcher. Currently the
 * Focus screen (the Bending Timer); the navigation host and the other destinations land here as
 * they are built.
 */
@Composable
fun FocusBloomApp() {
    FocusBloomTheme {
        val viewModel = koinViewModel<FocusViewModel>()
        FocusScreen(viewModel = viewModel)
    }
}
