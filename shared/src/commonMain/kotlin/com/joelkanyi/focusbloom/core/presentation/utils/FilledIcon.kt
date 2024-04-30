/*
 * Copyright 2023 Joel Kanyi.
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
package com.joelkanyi.focusbloom.core.presentation.utils

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.Tab
import focusbloom.shared.generated.resources.Res
import focusbloom.shared.generated.resources.add_filled
import focusbloom.shared.generated.resources.calendar_filled
import focusbloom.shared.generated.resources.home_filled
import focusbloom.shared.generated.resources.settings_filled
import focusbloom.shared.generated.resources.statistics_filled
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
@OptIn(ExperimentalResourceApi::class)
fun FilledIcon(item: Tab) = when (item.options.index) {
    (0u).toUShort() -> painterResource(Res.drawable.home_filled)
    (1u).toUShort() -> painterResource(Res.drawable.calendar_filled)
    (2u).toUShort() -> painterResource(Res.drawable.statistics_filled)
    (3u).toUShort() -> painterResource(Res.drawable.settings_filled)
    (4u).toUShort() -> painterResource(Res.drawable.add_filled)
    else -> painterResource(Res.drawable.home_filled)
}
