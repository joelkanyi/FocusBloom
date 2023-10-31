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
package com.joelkanyi.focusbloom.feature.taskprogress

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.joelkanyi.focusbloom.core.domain.model.SessionType
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.presentation.component.BloomTimerControls
import com.joelkanyi.focusbloom.core.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.core.presentation.component.TaskProgress
import com.joelkanyi.focusbloom.core.presentation.theme.LongBreakColor
import com.joelkanyi.focusbloom.core.presentation.theme.SessionColor
import com.joelkanyi.focusbloom.core.presentation.theme.ShortBreakColor
import com.joelkanyi.focusbloom.core.utils.UiEvents
import com.joelkanyi.focusbloom.core.utils.durationInMinutes
import com.joelkanyi.focusbloom.core.utils.sessionType
import com.joelkanyi.focusbloom.core.utils.toMillis
import com.joelkanyi.focusbloom.core.utils.toMinutes
import com.joelkanyi.focusbloom.core.utils.toPercentage
import com.joelkanyi.focusbloom.core.utils.toTimer
import com.joelkanyi.focusbloom.platform.StatusBarColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

data class TaskProgressScreen(
    val taskId: Int,
) : Screen, KoinComponent {

    @Composable
    override fun Content() {
        val screenModel = get<TaskProgressScreenModel>()
        val snackbarHostState = remember { SnackbarHostState() }
        val task = screenModel.task.collectAsState().value
        val navigator = LocalNavigator.currentOrThrow
        val timer = Timer.tickingTime.collectAsState().value
        val timerState = Timer.timerState.collectAsState().value
        val shortBreakColor = screenModel.shortBreakColor.collectAsState().value
        val longBreakColor = screenModel.longBreakColor.collectAsState().value
        val focusColor = screenModel.focusColor.collectAsState().value
        val focusTime = screenModel.focusTime.collectAsState().value ?: (25).toMillis()
        val shortBreakTime = screenModel.shortBreakTime.collectAsState().value ?: (5).toMillis()
        val longBreakTime = screenModel.longBreakTime.collectAsState().value ?: (15).toMillis()

        val containerColor = when (task?.current.sessionType()) {
            SessionType.Focus -> {
                if (focusColor == null || focusColor == 0L) {
                    Color(SessionColor)
                } else {
                    Color(
                        focusColor,
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
        StatusBarColors(
            statusBarColor = containerColor,
            navBarColor = containerColor,
        )
        LaunchedEffect(key1 = Unit) {
            screenModel.getRemindersStatus()
            screenModel.getTask(taskId)
            withContext(Dispatchers.Main.immediate) {
                Timer.eventsFlow.collect { event ->
                    when (event) {
                        is UiEvents.ShowSnackbar -> {
                            snackbarHostState.showSnackbar(event.message)
                        }

                        else -> {}
                    }
                }
            }
        }

        if (task?.completed == true) {
            SuccessfulCompletionOfTask(
                title = "Task Completed",
                message = "You have successfully completed this task",
                onConfirm = {
                    Timer.reset()
                    navigator.pop()
                },
            )
        }

        FocusTimeScreenContent(
            task = task,
            focusTime = focusTime,
            shortBreakTime = shortBreakTime,
            longBreakTime = longBreakTime,
            timerValue = timer,
            snackbarHostState = snackbarHostState,
            timerState = timerState,
            containerColor = containerColor,
            onClickNavigateBack = {
                navigator.pop()
            },
            onClickNext = {
                screenModel.moveToNextSessionOfTheTask()
            },
            onClickReset = {
                screenModel.resetCurrentSessionOfTheTask()
            },
            onClickAction = { state ->
                when (state) {
                    TimerState.Ticking -> {
                        Timer.pause()
                    }

                    TimerState.Paused -> {
                        Timer.resume()
                    }

                    TimerState.Stopped -> {
                        // screenModel.setTime(task?.focusTime ?: 20)
                    }

                    TimerState.Idle -> {
                        Timer.start(
                            update = {
                                screenModel.updateConsumedTime()
                            },
                            executeTasks = {
                                screenModel.executeTasks()
                            },
                        )
                        screenModel.resetAllTasksToInactive()
                        screenModel.updateActiveTask(taskId, true)
                    }

                    TimerState.Finished -> {
                        // screenModel.setTime(task?.focusTime ?: 20)
                    }
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusTimeScreenContent(
    focusTime: Long,
    shortBreakTime: Long,
    longBreakTime: Long,
    containerColor: Color,
    timerValue: Long,
    timerState: TimerState,
    task: Task?,
    snackbarHostState: SnackbarHostState,
    onClickNavigateBack: () -> Unit,
    onClickAction: (state: TimerState) -> Unit,
    onClickNext: () -> Unit,
    onClickReset: () -> Unit,
) {
    Scaffold(
        containerColor = containerColor,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            BloomTopAppBar(
                hasBackNavigation = true,
                navigationIcon = {
                    IconButton(onClick = onClickNavigateBack) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Add Task Back Button",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = containerColor,
                ),
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
        ) {
            if (task == null) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Task not found",
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(PaddingValues(horizontal = 16.dp)),
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(.85f),
                                        text = task.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                    )

                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 18.sp,
                                                ),
                                            ) {
                                                append("${task.currentCycle}")
                                            }
                                            append("/${task.focusSessions}")
                                        },
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        text = "Total: ${
                                        task.durationInMinutes(
                                            sessionTime = focusTime.toMinutes(),
                                            shortBreakTime = shortBreakTime.toMinutes(),
                                            longBreakTime = longBreakTime.toMinutes(),
                                            focusSessions = task.focusSessions,
                                        )
                                        } minutes",
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                    Text(
                                        text = when (task.current.sessionType()) {
                                            SessionType.Focus -> "${focusTime.toMinutes()} min"
                                            SessionType.ShortBreak -> "${shortBreakTime.toMinutes()} min"
                                            SessionType.LongBreak -> "${longBreakTime.toMinutes()} min"
                                        },
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            TaskProgress(
                                percentage = timerValue.toPercentage(
                                    when (task.current.sessionType()) {
                                        SessionType.Focus -> focusTime
                                        SessionType.ShortBreak -> shortBreakTime
                                        SessionType.LongBreak -> longBreakTime
                                    },
                                ),
                                radius = 40.dp,
                                content = timerValue.toTimer(),
                                mainColor = MaterialTheme.colorScheme.primary,
                                counterColor = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(48.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = when (task.current.sessionType()) {
                                SessionType.Focus -> "Focus Time"
                                SessionType.ShortBreak -> "Short Break"
                                SessionType.LongBreak -> "Long Break"
                            },
                            style = MaterialTheme.typography.displaySmall.copy(
                                color = MaterialTheme.colorScheme.onPrimary,
                            ),
                            textAlign = TextAlign.Center,
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(56.dp))
                        BloomTimerControls(
                            modifier = Modifier.fillMaxWidth(),
                            state = timerState,
                            onClickReset = onClickReset,
                            onClickNext = onClickNext,
                            onClickAction = onClickAction,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SuccessfulCompletionOfTask(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        icon = {
            Image(
                modifier = Modifier.size(48.dp),
                painter = painterResource("ic_complete.xml"),
                contentDescription = "Task Completed",
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = onConfirm,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    textAlign = TextAlign.Center,
                ),
            )
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                ),
            )
        },
        dismissButton = {},
        confirmButton = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onConfirm,
            ) {
                Text(
                    text = "OK",
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        },
    )
}
