/*
 * Copyright 2024 Joel Kanyi.
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
package com.joelkanyi.focusbloom.core.presentation.navigation

import focusbloom.shared.generated.resources.Res
import focusbloom.shared.generated.resources.add_filled
import focusbloom.shared.generated.resources.calendar_filled
import focusbloom.shared.generated.resources.calendar_outlined
import focusbloom.shared.generated.resources.home_filled
import focusbloom.shared.generated.resources.home_outlined
import focusbloom.shared.generated.resources.*
import focusbloom.shared.generated.resources.settings_filled
import focusbloom.shared.generated.resources.settings_outlined
import focusbloom.shared.generated.resources.statistics_filled
import focusbloom.shared.generated.resources.statistics_outlined
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class BottomNav(
    val label: StringResource,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource,
    val index: Int,
    val route: Any,
) {
    Home(
        label = Res.string.nav_home,
        selectedIcon = Res.drawable.home_filled,
        unselectedIcon = Res.drawable.home_outlined,
        route = Destinations.Home,
        index = 0,
    ),
    Calendar(
        label = Res.string.nav_calendar,
        selectedIcon = Res.drawable.calendar_filled,
        unselectedIcon = Res.drawable.calendar_outlined,
        route = Destinations.Calendar,
        index = 1,
    ),
    Statistics(
        label = Res.string.nav_statistics,
        selectedIcon = Res.drawable.statistics_filled,
        unselectedIcon = Res.drawable.statistics_outlined,
        route = Destinations.Statistics,
        index = 2,
    ),
    Settings(
        label = Res.string.nav_settings,
        selectedIcon = Res.drawable.settings_filled,
        unselectedIcon = Res.drawable.settings_outlined,
        route = Destinations.Settings,
        index = 3,
    ),
}

enum class NavRail(
    val label: StringResource,
    val icon: DrawableResource,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource,
    val route: Any,
) {
    Home(
        label = Res.string.nav_home,
        icon = Res.drawable.home_filled,
        selectedIcon = Res.drawable.home_filled,
        unselectedIcon = Res.drawable.home_outlined,
        route = Destinations.Home,
    ),
    Calendar(
        label = Res.string.nav_calendar,
        icon = Res.drawable.calendar_filled,
        selectedIcon = Res.drawable.calendar_filled,
        unselectedIcon = Res.drawable.calendar_outlined,
        route = Destinations.Calendar,
    ),
    AddTask(
        label = Res.string.nav_add_task,
        icon = Res.drawable.add_filled,
        selectedIcon = Res.drawable.add_filled,
        unselectedIcon = Res.drawable.add_filled,
        route = Destinations.AddTask(),
    ),
    Statistics(
        label = Res.string.nav_statistics,
        icon = Res.drawable.statistics_filled,
        selectedIcon = Res.drawable.statistics_filled,
        unselectedIcon = Res.drawable.statistics_outlined,
        route = Destinations.Statistics,
    ),
    Settings(
        label = Res.string.nav_settings,
        icon = Res.drawable.settings_filled,
        selectedIcon = Res.drawable.settings_filled,
        unselectedIcon = Res.drawable.settings_outlined,
        route = Destinations.Settings,
    ),
}
