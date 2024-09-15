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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.joelkanyi.focusbloom.feature.addtask.AddTaskScreen
import com.joelkanyi.focusbloom.feature.calendar.CalendarScreen
import com.joelkanyi.focusbloom.feature.home.AllTasksScreen
import com.joelkanyi.focusbloom.feature.home.HomeScreen
import com.joelkanyi.focusbloom.feature.onboarding.OnboardingScreen
import com.joelkanyi.focusbloom.feature.onboarding.UsernameScreen
import com.joelkanyi.focusbloom.feature.settings.SettingsScreen
import com.joelkanyi.focusbloom.feature.statistics.AllStatisticsScreen
import com.joelkanyi.focusbloom.feature.statistics.StatisticsScreen
import com.joelkanyi.focusbloom.feature.taskprogress.TaskProgressScreen

@Composable
fun AppNavHost(
    completedOnboarding: Boolean,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
         startDestination = if (completedOnboarding) Destinations.Home else Destinations.Onboarding
    ) {
        composable<Destinations.Onboarding> {
            OnboardingScreen(
                navController = navController
            )
        }

        composable<Destinations.Username> {
            UsernameScreen(
                navController = navController
            )
        }

        composable<Destinations.Home> {
            HomeScreen(
                navController = navController
            )
        }

        composable<Destinations.AllTasks> { backStackEntry ->
            val allTasks: Destinations.AllTasks = backStackEntry.toRoute()
             AllTasksScreen(
                 type = allTasks.type,
                 navController = navController
             )
        }

        composable<Destinations.AddTask> { backStackEntry ->
            val addTask: Destinations.AddTask = backStackEntry.toRoute()
            AddTaskScreen(
                taskId = addTask.taskId,
                navController = navController
            )
        }

        composable<Destinations.Calendar> {
            CalendarScreen(navController = navController)
        }

        composable<Destinations.Statistics> {
            StatisticsScreen(navController = navController)
        }

        composable<Destinations.AllStatistics> {
            AllStatisticsScreen(navController = navController)
        }

        composable<Destinations.Settings> {
            SettingsScreen(navController = navController)
        }

        composable<Destinations.TaskProgress> { backStackEntry ->
            val taskProgress: Destinations.TaskProgress = backStackEntry.toRoute()
            TaskProgressScreen(
                taskId = taskProgress.taskId,
                navController = navController
            )
        }
    }
}
