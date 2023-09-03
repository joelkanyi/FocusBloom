@file:OptIn(ExperimentalFoundationApi::class)

package com.joelkanyi.focusbloom.presentation.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.joelkanyi.focusbloom.domain.model.Task
import com.joelkanyi.focusbloom.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.presentation.utils.MAX
import com.joelkanyi.focusbloom.presentation.utils.MIN
import com.joelkanyi.focusbloom.presentation.utils.PositionedTask
import com.joelkanyi.focusbloom.presentation.utils.ScheduleSize
import com.joelkanyi.focusbloom.presentation.utils.SplitType
import com.joelkanyi.focusbloom.presentation.utils.arrangeTasks
import com.joelkanyi.focusbloom.presentation.utils.differenceBetweenDays
import com.joelkanyi.focusbloom.presentation.utils.differenceBetweenMinutes
import com.joelkanyi.focusbloom.presentation.utils.differenceBetweenWeeks
import com.joelkanyi.focusbloom.presentation.utils.dpToPx
import com.joelkanyi.focusbloom.presentation.utils.plusHours
import com.joelkanyi.focusbloom.presentation.utils.plusWeeks
import com.joelkanyi.focusbloom.presentation.utils.splitTasks
import com.joelkanyi.focusbloom.presentation.utils.taskData
import com.joelkanyi.focusbloom.presentation.utils.truncatedTo
import com.joelkanyi.samples.sampleTasks
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt

@Composable
fun CalendarScreen() {
    CalendarScreenContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreenContent() {
    Scaffold(
        topBar = {
            BloomTopAppBar(
                hasBackNavigation = false,
            ) {
                Text(text = "Calendar")
            }
        },
    ) { paddingValues ->
        var selectedDay by remember {
            mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(PaddingValues(horizontal = 16.dp)),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            /*HorizontalCalendarView(
                modifier = Modifier.fillMaxWidth(),
                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                unSelectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedCardColor = MaterialTheme.colorScheme.primary,
                unSelectedCardColor = MaterialTheme.colorScheme.surfaceVariant,
                onDayClick = { day ->
                    selectedDay = day.fullDate.localDate()
                    // Toast.makeText(context, day.toString(), Toast.LENGTH_SHORT).show()
                },
            )*/

            val todaysTasks =
                sampleTasks.filter { it.start.date.dayOfMonth == selectedDay.dayOfMonth }
            if (todaysTasks.isNotEmpty()) {
                Schedule(
                    tasks = todaysTasks.sortedBy { it.start },
                )
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "No tasks for today",
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
        }
    }
}

@Composable
fun BasicTask(
    positionedTask: PositionedTask,
    modifier: Modifier = Modifier,
) {
    val task = positionedTask.task
    val topRadius =
        if (positionedTask.splitType == SplitType.Start || positionedTask.splitType == SplitType.Both) 0.dp else 4.dp
    val bottomRadius =
        if (positionedTask.splitType == SplitType.End || positionedTask.splitType == SplitType.Both) 0.dp else 4.dp
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 2.dp,
                end = 2.dp,
                bottom = if (positionedTask.splitType == SplitType.End) 0.dp else 2.dp,
            )
            .clipToBounds()
            .background(
                color = Color(task.color),
                shape = RoundedCornerShape(
                    topStart = topRadius,
                    topEnd = topRadius,
                    bottomEnd = bottomRadius,
                    bottomStart = bottomRadius,
                ),
            )
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = "${task.start.time} - ${task.end.time}",
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Clip,
        )

        Text(
            text = task.name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        if (task.description != null) {
            Text(
                text = task.description!!,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun BasicSidebarLabel(
    time: LocalTime,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "${time.hour}:00",
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp),
    )
}

@Composable
fun ScheduleSidebar(
    hourHeight: Dp,
    modifier: Modifier = Modifier,
    minTime: LocalTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).time.MIN(),
    maxTime: LocalTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).time.MAX(),
    label: @Composable (time: LocalTime) -> Unit = { BasicSidebarLabel(time = it) },
) {
    val numMinutes = differenceBetweenMinutes(minTime, maxTime).toInt() + 1
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
                label(startTime.plusHours(i))
            }
        }
    }
}

@Composable
fun Schedule(
    tasks: List<Task>,
    modifier: Modifier = Modifier,
    taskContent: @Composable (positionedTask: PositionedTask) -> Unit = {
        BasicTask(
            positionedTask = it,
        )
    },
    timeLabel: @Composable (time: LocalTime) -> Unit = { BasicSidebarLabel(time = it) },
    minDate: LocalDate = tasks.minByOrNull(Task::start)?.start?.date ?: Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    maxDate: LocalDate = tasks.maxByOrNull(Task::end)?.end?.date ?: Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    minTime: LocalTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).time.MIN(),
    maxTime: LocalTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).time.MAX(),
    daySize: ScheduleSize = ScheduleSize.FixedSize(300.dp),
    hourSize: ScheduleSize = ScheduleSize.FixedSize(64.dp),
) {
    val numDays = 0 + 1
    val numMinutes = differenceBetweenMinutes(minTime, maxTime).toInt() + 1
    val numHours = numMinutes.toFloat() / 60f
    val verticalScrollState = rememberScrollState()
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

    val horizontalScrollState = rememberScrollState()
    var sidebarWidth by remember { mutableStateOf(0) }
    // var headerHeight by remember { mutableStateOf(0) }
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
                    hourHeight = hourHeight,
                    minTime = minTime,
                    maxTime = maxTime,
                    label = timeLabel,
                    modifier = Modifier
                        .verticalScroll(verticalScrollState)
                        .onGloballyPositioned { sidebarWidth = it.size.width },
                )
                BasicSchedule(
                    tasks = tasks,
                    taskContent = taskContent,
                    minDate = minDate,
                    maxDate = maxDate,
                    minTime = minTime,
                    maxTime = maxTime,
                    dayWidth = dayWidth,
                    hourHeight = hourHeight,
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(verticalScrollState)
                        .horizontalScroll(horizontalScrollState),
                )
            }
        }
    }
}

@Composable
fun BasicSchedule(
    tasks: List<Task>,
    modifier: Modifier = Modifier,
    taskContent: @Composable (positionedTask: PositionedTask) -> Unit = {
        BasicTask(
            positionedTask = it,
        )
    },
    minDate: LocalDate = tasks.minByOrNull(Task::start)?.start?.date ?: Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    maxDate: LocalDate = tasks.maxByOrNull(Task::end)?.end?.date ?: Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    minTime: LocalTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).time.MIN(),
    maxTime: LocalTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).time.MAX(),
    dayWidth: Dp,
    hourHeight: Dp,
) {
    val numDays = differenceBetweenDays(minDate, maxDate).toInt() + 1
    val numMinutes = differenceBetweenMinutes(minTime, maxTime).toInt() + 1
    val numHours = numMinutes / 60
    val dividerColor =
        if (androidx.compose.material.MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
    val positionedTasks =
        remember(tasks) { arrangeTasks(splitTasks(tasks.sortedBy(Task::start))).filter { it.end > minTime && it.start < maxTime } }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleWeek(
    tasks: List<Task>,
    setTitle: (String) -> Unit,
    pagerState: PagerState,
) {

}
