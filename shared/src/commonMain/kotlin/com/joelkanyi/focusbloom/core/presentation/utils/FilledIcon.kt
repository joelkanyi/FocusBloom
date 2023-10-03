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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
@OptIn(ExperimentalResourceApi::class)
fun FilledIcon(item: Tab) = when (item.options.index) {
    (0u).toUShort() -> painterResource("home_filled.xml")
    (1u).toUShort() -> painterResource("calendar_filled.xml")
    (2u).toUShort() -> painterResource("statistics_filled.xml")
    (3u).toUShort() -> painterResource("settings_filled.xml")
    (4u).toUShort() -> painterResource("add_filled.xml")
    else -> painterResource("home_filled.xml")
}
