package com.joelkanyi.focusbloom

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.joelkanyi.focusbloom.presentation.App

fun main() {
    application {
        Window(
            title = "Kmp App",
            onCloseRequest = ::exitApplication,
        ) {
            App()
        }
    }
}
