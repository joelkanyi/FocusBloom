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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import com.joelkanyi.focusbloom.core.domain.model.SessionType
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.presentation.component.BloomButton
import com.joelkanyi.focusbloom.core.presentation.component.BloomTab
import com.joelkanyi.focusbloom.core.presentation.component.TaskCard
import com.joelkanyi.focusbloom.core.presentation.component.TaskProgress
import com.joelkanyi.focusbloom.core.presentation.theme.LongBreakColor
import com.joelkanyi.focusbloom.core.presentation.theme.SessionColor
import com.joelkanyi.focusbloom.core.presentation.theme.ShortBreakColor
import com.joelkanyi.focusbloom.core.utils.koinViewModel
import com.joelkanyi.focusbloom.core.utils.pickFirstName
import com.joelkanyi.focusbloom.core.utils.sessionType
import com.joelkanyi.focusbloom.core.utils.taskCompleteMessage
import com.joelkanyi.focusbloom.core.utils.taskCompletionPercentage
import com.joelkanyi.focusbloom.core.utils.toTimer
import com.joelkanyi.focusbloom.core.utils.today
import com.joelkanyi.focusbloom.feature.home.component.TaskOptionsBottomSheet
import com.joelkanyi.focusbloom.feature.taskprogress.TaskProgressScreen
import com.joelkanyi.focusbloom.feature.taskprogress.Timer
import com.joelkanyi.focusbloom.feature.taskprogress.TimerState
import com.joelkanyi.focusbloom.platform.StatusBarColors
import focusbloom.shared.generated.resources.Res
import focusbloom.shared.generated.resources.il_completed
import focusbloom.shared.generated.resources.il_empty
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
) {
    StatusBarColors(
        statusBarColor = MaterialTheme.colorScheme.background,
        navBarColor = MaterialTheme.colorScheme.background,
    )
    val tasksState = viewModel.tasks.collectAsState().value
    val username = viewModel.username.collectAsState().value ?: ""
    val hourFormat = viewModel.hourFormat.collectAsState().value
    val sessionTime = viewModel.sessionTime.collectAsState().value ?: 25
    val shortBreakTime = viewModel.shortBreakTime.collectAsState().value ?: 5
    val longBreakTime = viewModel.longBreakTime.collectAsState().value ?: 15
    val navigator = LocalNavigator.currentOrThrow
    val tabNavigator = LocalTabNavigator.current
    val selectedTask = viewModel.selectedTask.collectAsState().value
    val openBottomSheet = viewModel.openBottomSheet.collectAsState().value
    val shortBreakColor = viewModel.shortBreakColor.collectAsState().value
    val longBreakColor = viewModel.longBreakColor.collectAsState().value
    val focusColor = viewModel.focusColor.collectAsState().value
    val timerState = Timer.timerState.collectAsState().value
    val tickingTime = Timer.tickingTime.collectAsState().value
    val bottomSheetState = rememberModalBottomSheetState()
    val remindersOn = viewModel.remindersOn.collectAsState().value

    LaunchedEffect(Unit) {
        when (remindersOn) {
            ReminderState.Loading -> {}
            is ReminderState.Success -> {
                if (remindersOn.reminderOn == null) {
                    viewModel.toggleReminder(1)
                }
            }
        }
    }

    if (openBottomSheet) {
        if (selectedTask != null) {
            TaskOptionsBottomSheet(
                type = if (selectedTask.date.date < today().date) {
                    "overdue"
                } else {
                    "today"
                },
                bottomSheetState = bottomSheetState,
                onClickCancel = {
                    viewModel.openBottomSheet(false)
                },
                onClickDelete = {
                    viewModel.deleteTask(it)
                },
                onDismissRequest = {
                    viewModel.openBottomSheet(false)
                    viewModel.selectTask(null)
                },
                onClickPushToTomorrow = {
                    viewModel.pushToTomorrow(it)
                },
                onClickPushToToday = {
                    viewModel.pushToToday(it)
                },
                onClickMarkAsCompleted = {
                    viewModel.markAsCompleted(it)
                },
                onClickEditTask = {
                    tabNavigator.current = BloomTab.AddTaskTab(taskId = it.id)
                },
                task = selectedTask,
            )
        }
    }

    HomeScreenContent(
        tasksState = tasksState,
        timerState = timerState,
        tickingTime = tickingTime,
        focusTimeColor = focusColor,
        shortBreakColor = shortBreakColor,
        longBreakColor = longBreakColor,
        hourFormat = hourFormat,
        sessionTime = sessionTime,
        shortBreakTime = shortBreakTime,
        longBreakTime = longBreakTime,
        username = username,
        onClickTask = {
            if (it.date.date < today().date) {
                viewModel.selectTask(it)
                viewModel.openBottomSheet(true)
            } else {
                navigator.parent?.push(TaskProgressScreen(taskId = it.id))
            }
        },
        onClickSeeAllTasks = {
            navigator.parent?.push(AllTasksScreen(it))
        },
        onClickTaskOptions = {
            viewModel.selectTask(it)
            viewModel.openBottomSheet(true)
        },
        onClickActiveTaskOptions = {
            when (timerState) {
                TimerState.Ticking -> {
                    Timer.pause()
                }

                TimerState.Paused -> {
                    Timer.resume()
                }

                else -> {}
            }
        },
        onClickAddTask = {
            tabNavigator.current = BloomTab.AddTaskTab()
        }
    )
}

@Composable
private fun HomeScreenContent(
    tasksState: TasksState,
    timerState: TimerState,
    tickingTime: Long,
    hourFormat: Int,
    sessionTime: Int,
    shortBreakTime: Int,
    longBreakTime: Int,
    username: String,
    onClickTask: (task: Task) -> Unit,
    onClickSeeAllTasks: (type: String) -> Unit,
    onClickTaskOptions: (task: Task) -> Unit,
    focusTimeColor: Long?,
    shortBreakColor: Long?,
    longBreakColor: Long?,
    onClickActiveTaskOptions: (task: Task) -> Unit,
    onClickAddTask: () -> Unit,
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
                    val overdueTasks = tasksState.overdueTasks
                    val activeTask = tasks.firstOrNull { it.active }
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        item {
                            if (activeTask != null) {
                                val containerColor = when (activeTask.current.sessionType()) {
                                    SessionType.Focus -> {
                                        if (focusTimeColor == null || focusTimeColor == 0L) {
                                            Color(SessionColor)
                                        } else {
                                            Color(
                                                focusTimeColor,
                                            )
                                        }
                                    }

                                    SessionType.LongBreak -> {
                                        if (longBreakColor == null || longBreakColor == 0L) {
                                            Color(LongBreakColor)
                                        } else {
                                            Color(
                                                longBreakColor,
                                            )
                                        }
                                    }

                                    SessionType.ShortBreak -> {
                                        if (shortBreakColor == null || shortBreakColor == 0L) {
                                            Color(ShortBreakColor)
                                        } else {
                                            Color(
                                                shortBreakColor,
                                            )
                                        }
                                    }
                                }
                                ActiveTaskCard(
                                    task = activeTask,
                                    onClick = onClickTask,
                                    containerColor = containerColor,
                                    onClickTaskOptions = onClickActiveTaskOptions,
                                    timerState = timerState,
                                    tickingTime = tickingTime,
                                )
                            }
                        }
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
                        if (overdueTasks.isNotEmpty()) {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        text = "Overdue Tasks (${overdueTasks.size})",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.error,
                                        ),
                                    )
                                    if (overdueTasks.size > 3) {
                                        Text(
                                            modifier = Modifier.clickable {
                                                onClickSeeAllTasks("overdue")
                                            },
                                            text = "See All",
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.error,
                                                fontSize = 16.sp,
                                            ),
                                        )
                                    }
                                }
                            }

                            items(
                                items = tasksState.overdueTasks.take(3),
                                key = { it.id },
                            ) {
                                TaskCard(
                                    type = "overdue",
                                    task = it,
                                    onClick = onClickTask,
                                    onShowTaskOption = onClickTaskOptions,
                                    hourFormat = hourFormat,
                                    focusSessions = it.focusSessions,
                                    sessionTime = sessionTime,
                                    shortBreakTime = shortBreakTime,
                                    longBreakTime = longBreakTime,
                                )
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
                                        Text(
                                            modifier = Modifier.clickable {
                                                onClickSeeAllTasks("today")
                                            },
                                            text = "See All",
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontSize = 16.sp,
                                            ),
                                        )
                                    }
                                }
                            }
                        }
                        if (tasks.all { it.completed }.not()) {
                            items(
                                items = tasks.take(3),
                                key = { it.id },
                            ) {
                                TaskCard(
                                    type = "today",
                                    task = it,
                                    onClick = onClickTask,
                                    onShowTaskOption = onClickTaskOptions,
                                    hourFormat = hourFormat,
                                    focusSessions = it.focusSessions,
                                    sessionTime = sessionTime,
                                    shortBreakTime = shortBreakTime,
                                    longBreakTime = longBreakTime,
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
                                        painter = painterResource(if (tasks.isEmpty()) Res.drawable.il_empty else Res.drawable.il_completed),
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
                                            "To add a task, simply tap the '+' button on the screen. Fill in the task details and tap 'Save'."
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

                                    Spacer(modifier = Modifier.height(24.dp))
                                    if (tasks.isEmpty()) {
                                        BloomButton(
                                            onClick = onClickAddTask,
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(horizontal = 24.dp),
                                                text = "Add Your First Task",
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                ),
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
    }
}

@Composable
fun ActiveTaskCard(
    task: Task,
    timerState: TimerState,
    tickingTime: Long,
    onClick: (task: Task) -> Unit,
    onClickTaskOptions: (task: Task) -> Unit,
    containerColor: Color,
) {
    Card(
        onClick = {
            onClick(task)
        },
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(.85f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${
                        when (task.current.sessionType()) {
                            SessionType.Focus -> {
                                "Focus Session"
                            }

                            SessionType.ShortBreak -> {
                                "Short Break"
                            }

                            SessionType.LongBreak -> {
                                "Long Break"
                            }
                        }
                    } - ${
                        tickingTime.toTimer()
                    }",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }
            IconButton(
                onClick = {
                    onClickTaskOptions(task)
                },
            ) {
                Icon(
                    modifier = Modifier,
                    imageVector = when (timerState) {
                        TimerState.Ticking -> {
                            Icons.Default.Pause
                        }

                        TimerState.Paused -> {
                            Icons.Default.PlayArrow
                        }

                        else -> {
                            Icons.Default.PlayArrow
                        }
                    },
                    contentDescription = "Play/Pause",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
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
                mainColor = MaterialTheme.colorScheme.primary,
                percentage = taskCompletionPercentage(tasks).toFloat(),
                counterColor = MaterialTheme.colorScheme.onSurface,
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
