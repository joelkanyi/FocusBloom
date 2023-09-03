package com.joelkanyi.focusbloom

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

@Composable
actual fun font(name: String, res: String, weight: FontWeight, style: FontStyle): Font {
    val context = LocalContext.current
    val id = context.resources.getIdentifier(res, "font", context.packageName)
    return Font(id, weight, style)
}