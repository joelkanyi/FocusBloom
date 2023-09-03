package com.joelkanyi.focusbloom

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
actual fun font(name: String, res: String, weight: FontWeight, style: FontStyle): Font =
    androidx.compose.ui.text.platform.Font("font/$res.ttf", weight, style)

actual class Platform actual constructor() {
    actual val platform: String = "Desktop"
}