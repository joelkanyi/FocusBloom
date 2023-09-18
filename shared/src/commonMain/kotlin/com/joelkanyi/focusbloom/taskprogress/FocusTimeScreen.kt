package com.joelkanyi.focusbloom.taskprogress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class FocusTimeScreen(
    val taskId: Int,
) : Screen, KoinComponent {
    private val screenModel: TaskProgressScreenModel by inject()

    @Composable
    override fun Content() {
        val snackbarHostState = remember { SnackbarHostState() }
        val task = screenModel.task.collectAsState().value
        val navigator = LocalNavigator.currentOrThrow
        val selectedTab = screenModel.selectedTab.collectAsState().value
        val timer = screenModel.tickingTime.collectAsState().value
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
            onClickNavigateBack = {
                navigator.pop()
            },
            isSelected = { title ->
                selectedTab == title
            },
            onClick = { title ->
                screenModel.selectTab(title)
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
    timerValue: Long,
    timerState: TimerState,
    task: Task?,
    snackbarHostState: SnackbarHostState,
    onClickNavigateBack: () -> Unit,
    isSelected: (title: String) -> Boolean,
    onClick: (title: String) -> Unit,
    onClickAction: (state: TimerState) -> Unit,
    onClickNext: () -> Unit,
    onClickReset: () -> Unit,
) {
    Scaffold(
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
                Column(
                    modifier = Modifier.padding(PaddingValues(horizontal = 16.dp)),
                ) {
                    /*Tabs(
                        isSelected = isSelected,
                        onClick = onClick,
                    )
                    Spacer(modifier = Modifier.height(16.dp))*/
                    LazyColumn {
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
}

@Composable
fun Tabs(
    isSelected: (title: String) -> Boolean,
    onClick: (title: String) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Tab(
            title = "Focus Time",
            isSelected = isSelected,
            onClick = onClick,
        )
        Tab(
            title = "Short Break",
            isSelected = isSelected,
            onClick = onClick,
        )
        Tab(
            title = "Long Break",
            isSelected = isSelected,
            onClick = onClick,
        )
    }
}

@Composable
fun Tab(
    title: String,
    isSelected: (title: String) -> Boolean,
    onClick: (title: String) -> Unit,
) {
    val color = if (isSelected(title)) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Box(
        modifier = Modifier
            .wrapContentSize(align = Alignment.Center)
            .clip(MaterialTheme.shapes.medium)
            .background(color = color)
            .then(
                if (isSelected(title).not()) {
                    Modifier.border(
                        width = .5.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .5f),
                        shape = MaterialTheme.shapes.medium,
                    )
                } else {
                    Modifier
                },
            )
            .clickable {
                onClick(title)
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                color = if (isSelected(title)) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .65f)
                },
            ),
        )
    }
}
