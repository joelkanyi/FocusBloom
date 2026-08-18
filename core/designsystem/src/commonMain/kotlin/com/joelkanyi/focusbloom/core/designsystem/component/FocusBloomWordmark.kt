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
package com.joelkanyi.focusbloom.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme

/**
 * The app wordmark, drawn from Jenga tokens only (no raw colors or type). First
 * FocusBloom* composite, proving Jenga components and tokens render end to end.
 */
@Composable
fun FocusBloomWordmark(modifier: Modifier = Modifier) {
    JengaText(
        text = "FocusBloom",
        modifier = modifier,
        style = JengaTheme.typography.headingSmall,
        color = JengaTheme.colors.textPrimary,
    )
}
