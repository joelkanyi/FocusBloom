package com.joelkanyi.focusbloom.feature.taskprogress

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
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
import com.joelkanyi.focusbloom.core.utils.UiEvents
import com.joelkanyi.focusbloom.core.utils.durationInMinutes
import com.joelkanyi.focusbloom.core.utils.sessionType
import com.joelkanyi.focusbloom.core.utils.toMinutes
import com.joelkanyi.focusbloom.core.utils.toPercentage
import com.joelkanyi.focusbloom.core.utils.toTimer
import com.joelkanyi.focusbloom.platform.StatusBarColors
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TaskProgressScreen(
    val taskId: Int,
) : Screen, KoinComponent {
    private val screenModel: TaskProgressScreenModel by inject()

    @Composable
    override fun Content() {
        val snackbarHostState = remember { SnackbarHostState() }
        val task = screenModel.task.collectAsState().value
        val navigator = LocalNavigator.currentOrThrow
        val timer = screenModel.tickingTime.collectAsState().value
        val shortBreakColor = screenModel.shortBreakColor.collectAsState().value
        val longBreakColor = screenModel.longBreakColor.collectAsState().value
        val focusColor = screenModel.focusColor.collectAsState().value

        val containerColor = when (task?.current.sessionType()) {
            SessionType.Focus -> focusColor?.let {
                Color(
                    it,
                )
            }

            SessionType.LongBreak -> longBreakColor?.let {
                Color(
                    it,
                )
            }

            SessionType.ShortBreak -> shortBreakColor?.let {
                Color(
                    it,
                )
            }
        }
        StatusBarColors(
            statusBarColor = containerColor ?: MaterialTheme.colorScheme.background,
            navBarColor = containerColor ?: MaterialTheme.colorScheme.background,
        )
        LaunchedEffect(key1 = Unit) {
            screenModel.getTask(taskId)
            screenModel.eventsFlow.collectLatest { event ->
                when (event) {
                    is UiEvents.ShowSnackbar -> {
                        snackbarHostState.showSnackbar(event.message)
                    }

                    is UiEvents.TimerEventFinished -> {
                        println("Timer Finished")
                        screenModel.executeTasks()
                    }

                    is UiEvents.TimerEventStarted -> {
                        println("Timer Started")
                    }

                    else -> {}
                }
            }
        }

        FocusTimeScreenContent(
            task = task,
            timerValue = timer,
            snackbarHostState = snackbarHostState,
            timerState = screenModel.timerState.collectAsState().value,
            containerColor = containerColor ?: MaterialTheme.colorScheme.background,
            onClickNavigateBack = {
                navigator.pop()
            },
            onClickNext = {
                screenModel.next()
            },
            onClickReset = {
                screenModel.reset()
            },
            onClickAction = { state ->
                when (state) {
                    TimerState.Ticking -> {
                        screenModel.pause()
                    }

                    TimerState.Paused -> {
                        screenModel.resume()
                    }

                    TimerState.Stopped -> {
                        // screenModel.setTime(task?.focusTime ?: 20)
                    }

                    TimerState.Idle -> {
                        screenModel.start()
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
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
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
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
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
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.End,
                                    ) {
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
                                        Text(
                                            text = when (task.current.sessionType()) {
                                                SessionType.Focus -> "${task.focusTime.toMinutes()} min"
                                                SessionType.ShortBreak -> "${task.shortBreakTime.toMinutes()} min"
                                                SessionType.LongBreak -> "${task.longBreakTime.toMinutes()} min"
                                            },
                                        )
                                    }
                                }

                                Text(
                                    text = "${task.durationInMinutes()} minutes",
                                    style = MaterialTheme.typography.bodySmall,
                                )
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
                                        SessionType.Focus -> task.focusTime
                                        SessionType.ShortBreak -> task.shortBreakTime
                                        SessionType.LongBreak -> task.longBreakTime
                                    },
                                ),
                                radius = 40.dp,
                                content = timerValue.toTimer(),
                                mainColor = MaterialTheme.colorScheme.primary,
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
                            style = MaterialTheme.typography.displaySmall,
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