package com.joelkanyi.focusbloom.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color

@Composable
actual fun StatusBarColors(
    statusBarColor: Color,
    navBarColor: Color,
) {
    SideEffect {
        // no-op
    }
}
