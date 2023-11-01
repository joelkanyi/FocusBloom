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
package com.joelkanyi.focusbloom.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.core.presentation.component.TaskCard
import com.joelkanyi.focusbloom.feature.home.component.TaskOptionsBottomSheet
import com.joelkanyi.focusbloom.feature.taskprogress.TaskProgressScreen
import com.joelkanyi.focusbloom.platform.StatusBarColors
import org.koin.compose.rememberKoinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

data class AllTasksScreen(
    val type: String,
) : Screen, KoinComponent {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = rememberKoinInject<HomeScreenModel>()
        StatusBarColors(
            statusBarColor = MaterialTheme.colorScheme.background,
            navBarColor = MaterialTheme.colorScheme.background,
        )
        val navigator = LocalNavigator.currentOrThrow
        val tasksState = screenModel.tasks.collectAsState().value
        val hourFormat = screenModel.hourFormat.collectAsState().value
        val sessionTime = screenModel.sessionTime.collectAsState().value ?: 25
        val shortBreakTime = screenModel.shortBreakTime.collectAsState().value ?: 5
        val longBreakTime = screenModel.longBreakTime.collectAsState().value ?: 15
        val selectedTask = screenModel.selectedTask.collectAsState().value
        val openBottomSheet = screenModel.openBottomSheet.collectAsState().value
        val bottomSheetState = rememberModalBottomSheetState()
        // val tabNavigator = LocalTabNavigator.current

        if (openBottomSheet) {
            if (selectedTask != null) {
                TaskOptionsBottomSheet(
                    type = type,
                    bottomSheetState = bottomSheetState,
                    onClickCancel = {
                        screenModel.openBottomSheet(false)
                    },
                    onClickDelete = {
                        screenModel.deleteTask(it)
                    },
                    onDismissRequest = {
                        screenModel.openBottomSheet(false)
                        screenModel.selectTask(null)
                    },
                    onClickPushToTomorrow = {
                        screenModel.pushToTomorrow(it)
                    },
                    onClickPushToToday = {
                        screenModel.pushToToday(it)
                    },
                    onClickMarkAsCompleted = {
                        screenModel.markAsCompleted(it)
                    },
                    onClickEditTask = {
                        /*tabNavigator.current = BloomTab.AddTaskTab(
                            taskId = it.id
                        )*/
                    },
                    task = selectedTask,
                )
            }
        }

        AllTasksScreenContent(
            type = type,
            tasksState = tasksState,
            timeFormat = hourFormat,
            sessionTime = sessionTime,
            shortBreakTime = shortBreakTime,
            longBreakTime = longBreakTime,
            onClickNavigateBack = {
                navigator.pop()
            },
            onClickTaskOptions = {
                screenModel.selectTask(it)
                screenModel.openBottomSheet(true)
            },
            onClickTask = {
                navigator.push(TaskProgressScreen(taskId = it.id))
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTasksScreenContent(
    tasksState: TasksState,
    timeFormat: Int,
    sessionTime: Int,
    shortBreakTime: Int,
    longBreakTime: Int,
    onClickNavigateBack: () -> Unit,
    onClickTaskOptions: (task: Task) -> Unit,
    onClickTask: (task: Task) -> Unit,
    type: String,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (tasksState) {
            is TasksState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            is TasksState.Success -> {
                val tasks = tasksState.tasks
                val overdueTasks = tasksState.overdueTasks
                Scaffold(
                    topBar = {
                        BloomTopAppBar(
                            hasBackNavigation = true,
                            navigationIcon = {
                                IconButton(onClick = onClickNavigateBack) {
                                    Icon(
                                        imageVector = Icons.Outlined.ArrowBack,
                                        contentDescription = "Back",
                                    )
                                }
                            },
                        ) {
                            Text(
                                text = "${
                                if (type == "today") "Today's" else "Overdue"
                                } Tasks (${
                                if (type == "today") tasks.size else overdueTasks.size
                                })",
                                color = if (type == "today") MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.error,
                            )
                        }
                    },
                ) { paddingValues ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(
                            items = if (type == "today") tasks else overdueTasks,
                            key = { it.id },
                        ) {
                            TaskCard(
                                type = type,
                                task = it,
                                hourFormat = timeFormat,
                                onClick = onClickTask,
                                onShowTaskOption = onClickTaskOptions,
                                focusSessions = it.focusSessions,
                                sessionTime = sessionTime,
                                shortBreakTime = shortBreakTime,
                                longBreakTime = longBreakTime,
                            )
                        }
                    }
                }
            }
        }
    }
}
