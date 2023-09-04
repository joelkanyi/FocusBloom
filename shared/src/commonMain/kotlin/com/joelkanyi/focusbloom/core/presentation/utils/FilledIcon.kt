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
