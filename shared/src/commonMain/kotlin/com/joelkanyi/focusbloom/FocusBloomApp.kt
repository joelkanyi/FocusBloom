package com.joelkanyi.focusbloom

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.joelkanyi.focusbloom.core.presentation.component.BloomNavigationRailBar
import com.joelkanyi.focusbloom.core.presentation.component.BloomTab
import com.joelkanyi.focusbloom.core.presentation.theme.FocusBloomTheme
import com.joelkanyi.focusbloom.core.presentation.utils.FilledIcon
import com.joelkanyi.focusbloom.platform.StatusBarColors

@OptIn(
    ExperimentalVoyagerApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class,
)
@Composable
fun FocusBloomApp() {
    val useDarkTheme = false
    FocusBloomTheme(
        useDarkTheme = useDarkTheme,
    ) {
        StatusBarColors(
            statusBarColor = MaterialTheme.colorScheme.background,
            navBarColor = MaterialTheme.colorScheme.background,
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            val windowSizeClass = calculateWindowSizeClass()
            val useNavRail = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact

            TabNavigator(
                BloomTab.HomeTab,
                tabDisposable = {
                    TabDisposable(
                        navigator = it,
                        tabs = listOf(
                            BloomTab.HomeTab,
                            BloomTab.StatisticsTab,
                            BloomTab.SettingsTab,
                            BloomTab.CalendarTab,
                        ),
                    )
                },
            ) {
                val tabNavigator = LocalTabNavigator.current
                val showNavRailOrBottomBar = true
                if (useNavRail) {
                    Row {
                        if (showNavRailOrBottomBar) {
                            BloomNavigationRailBar(
                                tabNavigator = it,
                                navRailItems = listOf(
                                    BloomTab.HomeTab,
                                    BloomTab.CalendarTab,
                                    BloomTab.AddTaskTab,
                                    BloomTab.StatisticsTab,
                                    BloomTab.SettingsTab,
                                ),
                            )
                        }

                        CurrentScreen()
                    }
                } else {
                    Scaffold(
                        content = {
                            CurrentTab()
                        },
                        floatingActionButtonPosition = FabPosition.Center,
                        floatingActionButton = {
                            FloatingActionButton(
                                modifier = Modifier
                                    .offset(y = 60.dp)
                                    .size(42.dp),
                                containerColor = MaterialTheme.colorScheme.primary,
                                onClick = {
                                    tabNavigator.current = BloomTab.AddTaskTab
                                },
                                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                                shape = CircleShape,
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp),
                                )
                            }
                        },
                        bottomBar = {
                            if (showNavRailOrBottomBar) {
                                BottomNavigation(
                                    backgroundColor = MaterialTheme.colorScheme.background,
                                ) {
                                    TabNavigationItem(BloomTab.HomeTab)
                                    TabNavigationItem(BloomTab.CalendarTab)
                                    TabNavigationItem(BloomTab.StatisticsTab)
                                    TabNavigationItem(BloomTab.SettingsTab)
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val isSelected = tabNavigator.current == tab

    BottomNavigationItem(
        modifier = Modifier.offset(
            x = when (tab.options.index) {
                (0u).toUShort() -> 0.dp
                (1u).toUShort() -> (-24).dp
                (2u).toUShort() -> 24.dp
                (3u).toUShort() -> 0.dp
                else -> 0.dp
            },
        ),
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = {
            tab.options.icon?.let {
                Icon(
                    painter = if (isSelected) {
                        FilledIcon(tab)
                    } else {
                        it
                    },
                    contentDescription = tab.options.title,
                )
            }
        },
    )
}
