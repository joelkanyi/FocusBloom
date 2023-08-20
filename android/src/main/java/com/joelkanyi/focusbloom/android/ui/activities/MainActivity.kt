package com.joelkanyi.focusbloom.android.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.joelkanyi.focusbloom.android.R
import com.joelkanyi.focusbloom.android.ui.screens.NavGraphs
import com.joelkanyi.focusbloom.android.ui.screens.destinations.AddTaskScreenDestination
import com.joelkanyi.focusbloom.android.ui.screens.destinations.CalendarScreenDestination
import com.joelkanyi.focusbloom.android.ui.screens.destinations.Destination
import com.joelkanyi.focusbloom.android.ui.screens.destinations.HomeScreenDestination
import com.joelkanyi.focusbloom.android.ui.screens.destinations.SettingsScreenDestination
import com.joelkanyi.focusbloom.android.ui.screens.destinations.StatisticsScreenDestination
import com.joelkanyi.focusbloom.android.ui.theme.FocusBloomTheme
import com.joelkanyi.focusbloom.android.ui.theme.Theme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine

@ExperimentalFoundationApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navHostEngine = rememberNavHostEngine()
            val newBackStackEntry by navController.currentBackStackEntryAsState()
            val route = newBackStackEntry?.destination?.route
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            FocusBloomTheme(theme = Theme.LIGHT_THEME.themeValue) {
                Scaffold(
                    bottomBar = {
                        if (route in listOf(
                                HomeScreenDestination.route,
                                CalendarScreenDestination.route,
                                StatisticsScreenDestination.route,
                                SettingsScreenDestination.route,
                            )
                        ) {
                            BottomNavigation(
                                backgroundColor = MaterialTheme.colorScheme.background,
                            ) {
                                navigationItems.forEachIndexed { index, item ->
                                    val isSelected =
                                        currentDestination?.route?.contains(item.destination.route) == true
                                    BottomNavigationItem(
                                        modifier = Modifier.offset(
                                            x = when (index) {
                                                0 -> 0.dp
                                                1 -> (-24).dp
                                                2 -> 24.dp
                                                3 -> 0.dp
                                                else -> 0.dp
                                            },
                                        ),
                                        icon = {
                                            Icon(
                                                painter = painterResource(
                                                    id = if (isSelected) {
                                                        item.iconFilled
                                                    } else {
                                                        item.iconOutlined
                                                    },
                                                ),
                                                contentDescription = item.label,
                                                modifier = Modifier.size(24.dp),
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = item.label,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontWeight = if (isSelected) {
                                                        FontWeight.SemiBold
                                                    } else {
                                                        FontWeight.Normal
                                                    },
                                                ),
                                            )
                                        },
                                        selected = isSelected,
                                        onClick = {
                                            navController.navigate(item.destination.route) {
                                                navController.graph.startDestinationRoute?.let { screen_route ->
                                                    popUpTo(screen_route) {
                                                        saveState = true
                                                    }
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                    )
                                }
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.Center,
                    floatingActionButton = {
                        FloatingActionButton(
                            modifier = Modifier
                                .offset(y = 60.dp)
                                .size(42.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            onClick = {
                                navController.navigate(AddTaskScreenDestination.route)
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
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            navController = navController,
                            engine = navHostEngine,
                        )
                    }
                }
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val iconFilled: Int,
    val iconOutlined: Int,
    val destination: Destination,
)

val navigationItems = listOf(
    NavigationItem(
        label = "Home",
        iconFilled = R.drawable.house_fill,
        iconOutlined = R.drawable.house_outlined,
        destination = HomeScreenDestination,
    ),
    NavigationItem(
        label = "Calendar",
        iconFilled = R.drawable.calendar_fill,
        iconOutlined = R.drawable.calendar_outline,
        destination = CalendarScreenDestination,
    ),
    NavigationItem(
        label = "Statistics",
        iconFilled = R.drawable.statistics_fill,
        iconOutlined = R.drawable.statistics_outlined,
        destination = StatisticsScreenDestination,
    ),
    NavigationItem(
        label = "Settings",
        iconFilled = R.drawable.settings_fill,
        iconOutlined = R.drawable.settings_outlined,
        destination = SettingsScreenDestination,
    ),
)

data class DrawerItem(
    val icon: ImageVector,
    val label: String,
)
