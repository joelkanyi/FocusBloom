/*
 * Copyright 2026 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joelkanyi.focusbloom.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.joelkanyi.jenga.foundation.color.jengaDarkColors
import io.github.joelkanyi.jenga.foundation.color.jengaLightColors
import io.github.joelkanyi.jenga.foundation.color.withBrand
import io.github.joelkanyi.jenga.theme.JengaTheme

/**
 * Editorial ink brand: jenga's neutral ink/paper scheme with a single restrained
 * bloom accent mapped to the brand role. Placeholder accent value; a single
 * point to tune the whole app's accent.
 */
private val BloomAccent = Color(0xFF2FA36B)

/**
 * The app theme. Wraps [JengaTheme] so every screen reads FocusBloom's tokens.
 * This is the only place the app configures Jenga.
 */
@Composable
fun FocusBloomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        jengaDarkColors().withBrand(BloomAccent)
    } else {
        jengaLightColors().withBrand(BloomAccent)
    }
    JengaTheme(
        darkTheme = darkTheme,
        colors = colors,
        content = content,
    )
}
