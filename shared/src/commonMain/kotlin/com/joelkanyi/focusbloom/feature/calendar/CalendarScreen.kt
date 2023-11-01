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
package com.joelkanyi.focusbloom.feature.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.presentation.component.BloomTab
import com.joelkanyi.focusbloom.core.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.core.utils.PositionedTask
import com.joelkanyi.focusbloom.core.utils.ScheduleSize
import com.joelkanyi.focusbloom.core.utils.SplitType
import com.joelkanyi.focusbloom.core.utils.arrangeTasks
import com.joelkanyi.focusbloom.core.utils.calculateEndTime
import com.joelkanyi.focusbloom.core.utils.calendarLocalDates
import com.joelkanyi.focusbloom.core.utils.differenceBetweenDays
import com.joelkanyi.focusbloom.core.utils.differenceBetweenMinutes
import com.joelkanyi.focusbloom.core.utils.dpToPx
import com.joelkanyi.focusbloom.core.utils.durationInMinutes
import com.joelkanyi.focusbloom.core.utils.formattedTimeBasedOnTimeFormat
import com.joelkanyi.focusbloom.core.utils.insideThisWeek
import com.joelkanyi.focusbloom.core.utils.max
import com.joelkanyi.focusbloom.core.utils.min
import com.joelkanyi.focusbloom.core.utils.plusHours
import com.joelkanyi.focusbloom.core.utils.prettyPrintedMonthAndYear
import com.joelkanyi.focusbloom.core.utils.prettyTimeDifference
import com.joelkanyi.focusbloom.core.utils.splitTasks
import com.joelkanyi.focusbloom.core.utils.taskColor
import com.joelkanyi.focusbloom.core.utils.taskData
import com.joelkanyi.focusbloom.core.utils.today
import com.joelkanyi.focusbloom.core.utils.truncatedTo
import com.joelkanyi.focusbloom.feature.home.component.TaskOptionsBottomSheet
import com.joelkanyi.focusbloom.platform.StatusBarColors
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    screenModel: CalendarScreenModel = koinInject(),
) {
    StatusBarColors(
        statusBarColor = MaterialTheme.colorScheme.background,
        navBarColor = MaterialTheme.colorScheme.background,
    )
    val coroutineScope = rememberCoroutineScope()
    val tasks = screenModel.tasks.collectAsState().value
    val selectedDay = screenModel.selectedDay.collectAsState().value
    val hourFormat = screenModel.hourFormat.collectAsState().value ?: 24
    val calendarPagerState = rememberLazyListState()
    val verticalScrollState = rememberScrollState()
    val sessionTime = screenModel.sessionTime.collectAsState().value ?: 25
    val shortBreakTime = screenModel.shortBreakTime.collectAsState().value ?: 5
    val longBreakTime = screenModel.longBreakTime.collectAsState().value ?: 15
    val selectedTask = screenModel.selectedTask.collectAsState().value
    val openBottomSheet = screenModel.openBottomSheet.collectAsState().value
    val bottomSheetState = rememberModalBottomSheetState()
    val tabNavigator = LocalTabNavigator.current

    LaunchedEffect(key1 = tasks, block = {
        calendarPagerState.animateScrollToItem(
            index = calendarLocalDates().indexOf(selectedDay),
            scrollOffset = 0,
        )
    })
    BoxWithConstraints {
        val windowSizeClass = calculateWindowSizeClass()
        val useDesktopSize = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact
        val hourSize = if (useDesktopSize) 90.dp else 92.dp
        val daySize = if (useDesktopSize) this.maxWidth - 72.dp else this.maxWidth - 72.dp

        if (openBottomSheet) {
            if (selectedTask != null) {
                TaskOptionsBottomSheet(
                    type = "calendar",
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
                    onClickPushToToday = {},
                    onClickMarkAsCompleted = {
                        screenModel.markAsCompleted(it)
                    },
                    onClickEditTask = {
                        tabNavigator.current = BloomTab.AddTaskTab(taskId = it.id)
                    },
                    task = selectedTask,
                )
            }
        }

        CalendarScreenContent(
            selectedDay = selectedDay,
            hourFormat = hourFormat,
            sessionTime = sessionTime,
            shortBreakTime = shortBreakTime,
            longBreakTime = longBreakTime,
            hourSize = ScheduleSize.FixedSize(hourSize),
            daySize = ScheduleSize.FixedSize(daySize),
            selectedDayTasks = tasks.filter {
                it.date.date == selectedDay
            },
            verticalScrollState = verticalScrollState,
            calendarPagerState = calendarPagerState,
            onClickThisWeek = {
                coroutineScope.launch {
                    calendarPagerState.animateScrollToItem(
                        index = calendarLocalDates().indexOf(
                            Clock.System.now()
                                .toLocalDateTime(TimeZone.currentSystemDefault()).date,
                        ),
                        scrollOffset = 0,
                    )
                    screenModel.setSelectedDay(
                        Clock.System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date,
                    )
                }
            },
            onSelectDay = {
                screenModel.setSelectedDay(it)
            },
            onShowTaskOption = {
                screenModel.selectTask(it)
                screenModel.openBottomSheet(true)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun CalendarScreenContent(
    verticalScrollState: ScrollState,
    calendarPagerState: LazyListState,
    hourSize: ScheduleSize,
    daySize: ScheduleSize,
    hourFormat: Int,
    sessionTime: Int,
    shortBreakTime: Int,
    longBreakTime: Int,
    selectedDayTasks: List<Task>,
    selectedDay: LocalDate,
    onClickThisWeek: () -> Unit,
    onSelectDay: (LocalDate) -> Unit,
    onShowTaskOption: (task: Task) -> Unit,
) {
    Scaffold(
        topBar = {
            BloomTopAppBar(
                hasBackNavigation = false,
                title = {
                    Text(text = "Calendar")
                },
                actions = {
                    AnimatedVisibility(selectedDay.insideThisWeek().not()) {
                        TextButton(
                            onClick = onClickThisWeek,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Icon(
                                    modifier = Modifier.size(18.dp),
                                    painter = painterResource("redo.xml"),
                                    contentDescription = "Today",
                                )
                                Text(
                                    text = "TODAY",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary,
                                        textDecoration = TextDecoration.Underline,
                                    ),
                                )
                            }
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(PaddingValues(horizontal = 16.dp)),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = selectedDay.prettyPrintedMonthAndYear(),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End,
                ),
            )
            LazyRow(
                state = calendarPagerState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(calendarLocalDates()) { date ->
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clipToBounds()
                            .background(
                                color = if (date == selectedDay) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(4.dp),
                            )
                            .clickable {
                                onSelectDay(date)
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Column {
                            Text(
                                text = date.dayOfWeek.name.substring(0, 3),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = if (date == selectedDay) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                ),
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                            )
                            Text(
                                text = date.dayOfMonth.toString(),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = if (date == selectedDay) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                ),
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                            )
                        }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                if (selectedDayTasks.isNotEmpty()) {
                    Schedule(
                        verticalScrollState = verticalScrollState,
                        hourFormat = hourFormat,
                        tasks = selectedDayTasks.sortedBy { it.start },
                        hourSize = hourSize,
                        daySize = daySize,
                        sessionTime = sessionTime,
                        shortBreakTime = shortBreakTime,
                        longBreakTime = longBreakTime,
                        onShowTaskOption = onShowTaskOption,
                    )
                } else {
                    Text(
                        text = "No tasks for ${
                        if (selectedDay == today().date) {
                            "today"
                        } else {
                            "this day"
                        }
                        }",
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTask(
    hourFormat: Int,
    sessionTime: Int,
    shortBreakTime: Int,
    longBreakTime: Int,
    positionedTask: PositionedTask,
    modifier: Modifier = Modifier,
    onShowTaskOption: (task: Task) -> Unit,
) {
    val task = positionedTask.task
    val end by remember {
        mutableStateOf(
            task.start.calculateEndTime(
                focusSessions = task.focusSessions,
                sessionTime = sessionTime,
                shortBreakTime = shortBreakTime,
                longBreakTime = longBreakTime,
            ),
        )
    }
    val topRadius =
        if (positionedTask.splitType == SplitType.Start || positionedTask.splitType == SplitType.Both) 0.dp else 2.dp
    val bottomRadius =
        if (positionedTask.splitType == SplitType.End || positionedTask.splitType == SplitType.Both) 0.dp else 2.dp
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 2.dp,
                end = 2.dp,
                bottom = if (positionedTask.splitType == SplitType.End) 0.dp else 2.dp,
            )
            .clipToBounds()
            .padding(4.dp),
        shape = RoundedCornerShape(
            topStart = topRadius,
            topEnd = topRadius,
            bottomEnd = bottomRadius,
            bottomStart = bottomRadius,
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(task.type.taskColor()),
        ),
        onClick = {
            onShowTaskOption(task)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(.85f),
                    text = task.name,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp,
                    ),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onShowTaskOption(task)
                        },
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Task Options",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
            if (task.description != null) {
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 12.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${
                    task.durationInMinutes(
                        focusSessions = task.focusSessions,
                        sessionTime = sessionTime,
                        shortBreakTime = shortBreakTime,
                        longBreakTime = longBreakTime,
                    )
                    } minutes",
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                    ),
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = prettyTimeDifference(
                        start = task.start,
                        end = end,
                        timeFormat = hourFormat,
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}

@Composable
fun BasicSidebarLabel(hourFormat: Int, time: LocalTime, modifier: Modifier = Modifier) {
    Text(
        text = time.formattedTimeBasedOnTimeFormat(hourFormat),
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp),
        style = MaterialTheme.typography.labelMedium.copy(
            fontSize = 12.sp,
        ),
    )
}

@Composable
fun ScheduleSidebar(
    hourFormat: Int,
    hourHeight: Dp,
    modifier: Modifier = Modifier,
    minTime: LocalTime = min(),
    maxTime: LocalTime = max(),
    label: @Composable (hourFormat: Int, time: LocalTime) -> Unit = { _, time ->
        BasicSidebarLabel(
            hourFormat = hourFormat,
            time = time,
        )
    },
) {
    val numMinutes = differenceBetweenMinutes(minTime, maxTime) + 1
    val numHours = numMinutes / 60
    val firstHour = minTime.truncatedTo()
    val firstHourOffsetMinutes =
        if (firstHour == minTime) 0 else differenceBetweenMinutes(minTime, firstHour.plusHours(1))
    val firstHourOffset = hourHeight * (firstHourOffsetMinutes / 60f)
    val startTime = if (firstHour == minTime) firstHour else firstHour.plusHours(1)
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(firstHourOffset))
        repeat(numHours) { i ->
            Box(modifier = Modifier.height(hourHeight)) {
                label(
                    hourFormat,
                    startTime.plusHours(i),
                )
            }
        }
    }
}

@Composable
fun Schedule(
    hourFormat: Int,
    tasks: List<Task>,
    sessionTime: Int,
    shortBreakTime: Int,
    longBreakTime: Int,
    onShowTaskOption: (task: Task) -> Unit,
    verticalScrollState: ScrollState,
    modifier: Modifier = Modifier,
    taskContent: @Composable (
        positionedTask: PositionedTask,
    ) -> Unit = { positionedTask ->
        BasicTask(
            hourFormat = hourFormat,
            positionedTask = positionedTask,
            sessionTime = sessionTime,
            shortBreakTime = shortBreakTime,
            longBreakTime = longBreakTime,
            onShowTaskOption = onShowTaskOption,
        )
    },
    timeLabel: @Composable (hourFormat: Int, time: LocalTime) -> Unit = { hrFormat, time ->
        BasicSidebarLabel(
            hourFormat = hrFormat,
            time = time,
        )
    },
    minDate: LocalDate = tasks.minByOrNull(Task::start)?.start?.date ?: Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    // maxDate: LocalDate = tasks.maxByOrNull(Task::end)?.end?.date ?: Clock.System.now()
    // .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    maxDate: LocalDate = tasks.map {
        it.start.calculateEndTime(
            focusSessions = it.focusSessions,
            sessionTime = sessionTime,
            shortBreakTime = shortBreakTime,
            longBreakTime = longBreakTime,
        )
    }.maxOfOrNull { it.date } ?: today().date,
    minTime: LocalTime = min(),
    maxTime: LocalTime = max(),
    daySize: ScheduleSize,
    hourSize: ScheduleSize,
) {
    val numDays = 0 + 1
    val numMinutes = differenceBetweenMinutes(minTime, maxTime) + 1
    val numHours = numMinutes.toFloat() / 60f
    val scope = rememberCoroutineScope()

    /**
     * Scroll to the closest task
     * */
    // LaunchedEffect(key1 = tasks, block = {
    when (hourSize) {
        is ScheduleSize.Adaptive -> {
            val task = tasks.minByOrNull { it.start }
            if (task != null) {
                val taskStartMinutes = differenceBetweenMinutes(minTime, task.start.time)
                val taskStartHours = taskStartMinutes / 60f
                val taskStartOffset = taskStartHours * hourSize.minSize.dpToPx()
                LaunchedEffect(key1 = tasks, block = {
                    scope.launch {
                        verticalScrollState.animateScrollTo(taskStartOffset.roundToInt())
                    }
                })
            }
        }

        is ScheduleSize.FixedCount -> {
            val task = tasks.minByOrNull { it.start }
            if (task != null) {
                val taskStartMinutes = differenceBetweenMinutes(minTime, task.start.time)
                val taskStartHours = taskStartMinutes / 60f
                val taskStartOffset = taskStartHours * hourSize.count.dp
                LaunchedEffect(key1 = tasks, block = {
                    scope.launch {
                        verticalScrollState.animateScrollTo(taskStartOffset.value.roundToInt())
                    }
                })
            }
        }

        is ScheduleSize.FixedSize -> {
            // Scroll to the closest task
            val task = tasks.minByOrNull { it.start }
            if (task != null) {
                val taskStartMinutes = differenceBetweenMinutes(minTime, task.start.time)
                val taskStartHours = taskStartMinutes / 60f
                val taskStartOffset = taskStartHours * hourSize.size.dpToPx()
                LaunchedEffect(key1 = tasks, block = {
                    scope.launch {
                        verticalScrollState.animateScrollTo(taskStartOffset.roundToInt())
                    }
                })
            }
        }
    }
    // })

    var sidebarWidth by remember { mutableStateOf(0) }
    BoxWithConstraints(modifier = modifier) {
        val dayWidth: Dp = when (daySize) {
            is ScheduleSize.FixedSize -> daySize.size
            is ScheduleSize.FixedCount -> with(LocalDensity.current) { ((constraints.maxWidth - sidebarWidth) / daySize.count).toDp() }
            is ScheduleSize.Adaptive -> with(LocalDensity.current) {
                maxOf(
                    ((constraints.maxWidth - sidebarWidth) / numDays).toDp(),
                    daySize.minSize,
                )
            }
        }
        val hourHeight: Dp = when (hourSize) {
            is ScheduleSize.FixedSize -> hourSize.size
            is ScheduleSize.FixedCount -> with(LocalDensity.current) { ((constraints.maxHeight) / hourSize.count).toDp() }
            is ScheduleSize.Adaptive -> with(LocalDensity.current) {
                maxOf(
                    ((constraints.maxHeight) / numHours).toDp(),
                    hourSize.minSize,
                )
            }
        }
        Column(modifier = modifier) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Start),
            ) {
                ScheduleSidebar(
                    hourFormat = hourFormat,
                    hourHeight = hourHeight,
                    minTime = minTime,
                    maxTime = maxTime,
                    label = timeLabel,
                    modifier = Modifier
                        .verticalScroll(verticalScrollState)
                        .onGloballyPositioned { sidebarWidth = it.size.width },
                )
                BasicSchedule(
                    hourFormat = hourFormat,
                    tasks = tasks,
                    taskContent = taskContent,
                    sessionTime = sessionTime,
                    shortBreakTime = shortBreakTime,
                    longBreakTime = longBreakTime,
                    minDate = minDate,
                    maxDate = maxDate,
                    minTime = minTime,
                    maxTime = maxTime,
                    dayWidth = dayWidth,
                    hourHeight = hourHeight,
                    onShowTaskOption = onShowTaskOption,
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(verticalScrollState),
                )
            }
        }
    }
}

@Composable
fun BasicSchedule(
    hourFormat: Int,
    sessionTime: Int,
    shortBreakTime: Int,
    longBreakTime: Int,
    tasks: List<Task>,
    onShowTaskOption: (task: Task) -> Unit,
    modifier: Modifier = Modifier,
    taskContent: @Composable (positionedTask: PositionedTask) -> Unit = {
        BasicTask(
            hourFormat = hourFormat,
            positionedTask = it,
            sessionTime = sessionTime,
            shortBreakTime = shortBreakTime,
            longBreakTime = longBreakTime,
            onShowTaskOption = onShowTaskOption,
        )
    },
    minDate: LocalDate = tasks.minByOrNull(Task::start)?.start?.date ?: Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    maxDate: LocalDate = tasks.map {
        it.start.calculateEndTime(
            focusSessions = it.focusSessions,
            sessionTime = sessionTime,
            shortBreakTime = shortBreakTime,
            longBreakTime = longBreakTime,
        )
    }.maxOfOrNull { it.date } ?: today().date,
    minTime: LocalTime = min(),
    maxTime: LocalTime = max(),
    dayWidth: Dp,
    hourHeight: Dp,
) {
    val numDays = differenceBetweenDays(minDate, maxDate) + 1
    val numMinutes = differenceBetweenMinutes(minTime, maxTime) + 1
    val dividerColor =
        if (androidx.compose.material.MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
    val positionedTasks =
        remember(tasks) {
            arrangeTasks(
                splitTasks(
                    tasks = tasks.sortedBy(Task::start),
                    sessionTime = sessionTime,
                    shortBreakTime = shortBreakTime,
                    longBreakTime = longBreakTime,
                ),
            ).filter { it.end > minTime && it.start < maxTime }
        }
    Layout(
        content = {
            positionedTasks.forEach { positionedTask ->
                Box(modifier = Modifier.taskData(positionedTask)) {
                    taskContent(positionedTask)
                }
            }
        },
        modifier = modifier
            .offset(y = (16).dp)
            .drawBehind {
                repeat(23) {
                    drawLine(
                        dividerColor,
                        start = Offset(0f, (it + 1) * hourHeight.toPx()),
                        end = Offset(size.width, (it + 1) * hourHeight.toPx()),
                        strokeWidth = 1.dp.toPx(),
                    )
                }
            },
    ) { measureables, constraints ->
        val height = (hourHeight.toPx() * (numMinutes / 60f)).roundToInt()
        val width = dayWidth.roundToPx() * numDays
        val placeablesWithTasks = measureables.map { measurable ->
            val splitTask = measurable.parentData as PositionedTask
            val taskDurationMinutes =
                differenceBetweenMinutes(splitTask.start, minOf(splitTask.end, maxTime))
            val taskHeight = ((taskDurationMinutes / 60f) * hourHeight.toPx()).roundToInt()
            val taskWidth =
                ((splitTask.colSpan.toFloat() / splitTask.colTotal.toFloat()) * dayWidth.toPx()).roundToInt()
            val placeable = measurable.measure(
                constraints.copy(
                    minWidth = taskWidth,
                    maxWidth = taskWidth,
                    minHeight = taskHeight,
                    maxHeight = taskHeight,
                ),
            )
            Pair(placeable, splitTask)
        }
        layout(width, height) {
            placeablesWithTasks.forEach { (placeable, splitTask) ->
                val taskOffsetMinutes = if (splitTask.start > minTime) {
                    differenceBetweenMinutes(
                        minTime,
                        splitTask.start,
                    )
                } else {
                    0
                }
                val taskY = ((taskOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()
                val taskX =
                    (splitTask.col * (dayWidth.toPx() / splitTask.colTotal.toFloat())).roundToInt()
                placeable.place(taskX, taskY)
            }
        }
    }
}
