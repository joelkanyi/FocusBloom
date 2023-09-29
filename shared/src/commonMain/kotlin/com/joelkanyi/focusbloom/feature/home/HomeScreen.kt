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

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.currentOrThrow
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.presentation.component.TaskCard
import com.joelkanyi.focusbloom.core.presentation.component.TaskProgress
import com.joelkanyi.focusbloom.core.utils.LocalAppNavigator
import com.joelkanyi.focusbloom.core.utils.pickFirstName
import com.joelkanyi.focusbloom.core.utils.taskCompleteMessage
import com.joelkanyi.focusbloom.core.utils.taskCompletionPercentage
import com.joelkanyi.focusbloom.feature.home.component.TaskOptionsBottomSheet
import com.joelkanyi.focusbloom.feature.taskprogress.TaskProgressScreen
import com.joelkanyi.focusbloom.platform.StatusBarColors
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.rememberKoinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val screenModel: HomeScreenModel = rememberKoinInject()

    StatusBarColors(
        statusBarColor = MaterialTheme.colorScheme.background,
        navBarColor = MaterialTheme.colorScheme.background,
    )
    val tasksState = screenModel.tasks.collectAsState().value
    val username = screenModel.username.collectAsState().value ?: ""
    val hourFormat = screenModel.hourFormat.collectAsState().value
    val navigator = LocalAppNavigator.currentOrThrow
    val selectedTask = screenModel.selectedTask.collectAsState().value
    val openBottomSheet = screenModel.openBottomSheet.collectAsState().value

    val bottomSheetState = rememberModalBottomSheetState()

    if (openBottomSheet) {
        if (selectedTask != null) {
            TaskOptionsBottomSheet(
                bottomSheetState = bottomSheetState,
                onClickCancel = {
                    screenModel.openBottomSheet(false)
                },
                onClickSave = {
                    screenModel.updateTask(it)
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
                onClickMarkAsCompleted = {
                    screenModel.markAsCompleted(it)
                },
                task = selectedTask,
            )
        }
    }

    HomeScreenContent(
        tasksState = tasksState,
        hourFormat = hourFormat,
        username = username,
        onClickTask = {
            navigator.push(TaskProgressScreen(taskId = it.id))
        },
        onClickSeeAllTasks = {
            navigator.push(AllTasksScreen())
        },
        onClickTaskOptions = {
            screenModel.selectTask(it)
            screenModel.openBottomSheet(true)
        },
    )
}

@Composable
fun Option(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            modifier = Modifier,
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )
        Text(
            modifier = Modifier,
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            ),
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun HomeScreenContent(
    tasksState: TasksState,
    hourFormat: Int,
    username: String,
    onClickTask: (task: Task) -> Unit,
    onClickSeeAllTasks: () -> Unit,
    onClickTaskOptions: (task: Task) -> Unit,
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            when (tasksState) {
                TasksState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is TasksState.Success -> {
                    val tasks = tasksState.tasks.sortedByDescending { it.completed.not() }
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        item {
                            Text(
                                text = "Hello, ${username.pickFirstName()}!",
                                style = MaterialTheme.typography.displaySmall,
                            )
                        }
                        item {
                            if (tasks.isNotEmpty() && tasks.all { it.completed }.not()) {
                                TodayTaskProgressCard(tasks = tasks)
                            }
                        }
                        if (tasks.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        text = "Today's Tasks (${tasks.size})",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                        ),
                                    )
                                    if (tasks.size > 3) {
                                        TextButton(onClick = onClickSeeAllTasks) {
                                            Text(
                                                text = "See All",
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.primary,
                                                ),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        if (tasks.all { it.completed }.not()) {
                            items(tasks.take(3)) {
                                TaskCard(
                                    task = it,
                                    onClick = onClickTask,
                                    onShowTaskOption = onClickTaskOptions,
                                    hourFormat = hourFormat,
                                )
                            }
                        }

                        if (tasks.all { it.completed } || tasks.isEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = CenterHorizontally,
                                ) {
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Image(
                                        modifier = Modifier
                                            .size(300.dp)
                                            .align(CenterHorizontally),
                                        painter = painterResource(
                                            if (tasks.isEmpty()) "il_empty.xml" else "il_completed.xml",
                                        ),
                                        contentDescription = null,
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(CenterHorizontally),
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                        ),
                                        text = if (tasks.isEmpty()) {
                                            "Start your day productively! Add your first task."
                                        } else if (tasks.all { it.completed }) {
                                            "Great job! You've finished all your tasks for today."
                                        } else {
                                            ""
                                        },
                                        textAlign = TextAlign.Center,
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        modifier = Modifier
                                            .padding(horizontal = 24.dp)
                                            .fillMaxWidth(),
                                        text = if (tasks.isEmpty()) {
                                            "To add a task, simply tap the '+' button at the bottom of the screen. Fill in the task details and tap 'Save'."
                                        } else if (tasks.all { it.completed }) {
                                            "Now, take some time to have fun, recharge, maybe do some exercise, and consider opening your calendar to plan for tomorrow's tasks. Keep up the fantastic work!"
                                        } else {
                                            ""
                                        },
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontSize = 14.sp,
                                        ),
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TodayTaskProgressCard(tasks: List<Task>) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TaskProgress(
                mainColor = MaterialTheme.colorScheme.secondary,
                percentage = taskCompletionPercentage(tasks).toFloat(),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = taskCompleteMessage(tasks),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${tasks.filter { it.completed }.size} of ${tasks.size} tasks completed",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                )
            }
        }
    }
}
