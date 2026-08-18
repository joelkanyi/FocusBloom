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
package com.joelkanyi.focusbloom.shell

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.joelkanyi.focusbloom.core.designsystem.component.FocusBloomWordmark
import com.joelkanyi.focusbloom.core.designsystem.theme.FocusBloomTheme

/**
 * The shared application root, rendered identically by every platform launcher. For now an empty
 * themed surface with the wordmark, proving the design system renders inside a real app. Features
 * and the navigation host land here as they are built.
 */
@Composable
fun FocusBloomApp() {
    FocusBloomTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            FocusBloomWordmark()
        }
    }
}
