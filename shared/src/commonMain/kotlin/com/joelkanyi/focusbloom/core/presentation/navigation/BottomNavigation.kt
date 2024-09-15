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
import focusbloom.shared.generated.resources.settings_filled
import focusbloom.shared.generated.resources.settings_outlined
import focusbloom.shared.generated.resources.statistics_filled
import focusbloom.shared.generated.resources.statistics_outlined
import org.jetbrains.compose.resources.DrawableResource

enum class BottomNav(
    val label: String,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource,
    val index: Int,
    val route: Any,
) {
    Home(
        label = "Home",
        selectedIcon = Res.drawable.home_filled,
        unselectedIcon = Res.drawable.home_outlined,
        route = Destinations.Home,
        index = 0,
    ),
    Calendar(
        label = "Calendar",
        selectedIcon = Res.drawable.calendar_filled,
        unselectedIcon = Res.drawable.calendar_outlined,
        route = Destinations.Calendar,
        index = 1,
    ),
    Statistics(
        label = "Statistics",
        selectedIcon = Res.drawable.statistics_filled,
        unselectedIcon = Res.drawable.statistics_outlined,
        route = Destinations.Statistics,
        index = 2,
    ),
    Settings(
        label = "Settings",
        selectedIcon = Res.drawable.settings_filled,
        unselectedIcon = Res.drawable.settings_outlined,
        route = Destinations.Settings,
        index = 3,
    ),
}

enum class NavRail(
    val label: String,
    val icon: DrawableResource,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource,
    val route: Any,
) {
    Home(
        label = "Home",
        icon = Res.drawable.home_filled,
        selectedIcon = Res.drawable.home_filled,
        unselectedIcon = Res.drawable.home_outlined,
        route = Destinations.Home,
    ),
    Calendar(
        label = "Calendar",
        icon = Res.drawable.calendar_filled,
        selectedIcon = Res.drawable.calendar_filled,
        unselectedIcon = Res.drawable.calendar_outlined,
        route = Destinations.Calendar,
    ),
    AddTask(
        label = "Add Task",
        icon = Res.drawable.add_filled,
        selectedIcon = Res.drawable.add_filled,
        unselectedIcon = Res.drawable.add_filled,
        route = Destinations.AddTask(),
    ),
    Statistics(
        label = "Statistics",
        icon = Res.drawable.statistics_filled,
        selectedIcon = Res.drawable.statistics_filled,
        unselectedIcon = Res.drawable.statistics_outlined,
        route = Destinations.Statistics,
    ),
    Settings(
        label = "Settings",
        icon = Res.drawable.settings_filled,
        selectedIcon = Res.drawable.settings_filled,
        unselectedIcon = Res.drawable.settings_outlined,
        route = Destinations.Settings,
    ),
}
