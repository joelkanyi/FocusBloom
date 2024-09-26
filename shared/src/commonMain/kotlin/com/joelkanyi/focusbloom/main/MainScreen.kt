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
package com.joelkanyi.focusbloom.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.joelkanyi.focusbloom.core.presentation.component.BloomNavigationRailBar
import com.joelkanyi.focusbloom.core.presentation.navigation.AppNavHost
import com.joelkanyi.focusbloom.core.presentation.navigation.BottomNav
import com.joelkanyi.focusbloom.core.presentation.navigation.Destinations
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    onBoardingCompleted: Boolean,
) {
    val windowSizeClass = calculateWindowSizeClass()
    val useNavRail = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.substringBefore("?")
        ?: Destinations.Onboarding::class.qualifiedName.orEmpty()
    val showBottomNavigation =
        currentRoute in BottomNav.entries.map { it.route::class.qualifiedName }
    val addTask = Destinations.AddTask::class.qualifiedName?.substringBefore("?")

    if (useNavRail) {
        Row {
            if (onBoardingCompleted) {
                BloomNavigationRailBar(
                    navController = navController,
                )
            }
            AppNavHost(
                navController = navController,
                completedOnboarding = onBoardingCompleted,
            )
        }
    } else {
        Scaffold(
            content = { innerPadding ->
                AppNavHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    completedOnboarding = onBoardingCompleted,
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                if (showBottomNavigation || currentRoute == addTask) {
                    FloatingActionButton(
                        modifier = Modifier
                        .offset(y = 60.dp)
                            .size(42.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        onClick = {
                            navController.navigate(Destinations.AddTask())
                        },
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                        ),
                        shape = CircleShape,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Task",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }

            },
            bottomBar = {

                if (showBottomNavigation || currentRoute == addTask) {
                    BottomNavigation(
                        backgroundColor = MaterialTheme.colorScheme.background,
                    ) {
                        BottomNav.entries
                            .forEach { navigationItem ->
                                val isSelected by remember(currentRoute) {
                                    derivedStateOf { currentRoute == navigationItem.route::class.qualifiedName }
                                }
                                BottomNavigationItem(
                                    modifier = Modifier
                                        .testTag(navigationItem.name)
                                        .offset(
                                            x = when (navigationItem.index) {
                                                0 -> 0.dp
                                                1 -> (-24).dp
                                                2 -> 24.dp
                                                3 -> 0.dp
                                                else -> 0.dp
                                            },
                                        ),
                                    selected = isSelected,
                                    label = {
                                        Text(
                                            text = navigationItem.label,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(if (isSelected) navigationItem.selectedIcon else navigationItem.unselectedIcon),
                                            contentDescription = navigationItem.label,
                                            tint = if (isSelected) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.onBackground
                                            },
                                        )
                                    },
                                    onClick = {
                                        navController.navigate(navigationItem.route)
                                    },
                                )
                            }
                    }
                }
            },
        )
    }
}
