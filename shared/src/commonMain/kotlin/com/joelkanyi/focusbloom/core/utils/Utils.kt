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
package com.joelkanyi.focusbloom.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import cafe.adriel.voyager.navigator.Navigator
import com.joelkanyi.focusbloom.core.domain.model.SessionType
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.model.taskTypes
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.jvm.JvmInline

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

fun differenceBetweenMinutes(minTime: LocalTime, maxTime: LocalTime): Int {
    return (maxTime.hour - minTime.hour) * 60
}

fun differenceBetweenDays(
    minDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    maxDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
): Int {
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
    return (maxDate.dayOfMonth - minDate.dayOfMonth)
}

fun LocalDate.plusDays(days: Int): LocalDate {
    return this.plus(days, DateTimeUnit.DAY)
}

fun LocalDateTime.plusDays(days: Int): LocalDateTime {
    return this.date.plus(days, DateTimeUnit.DAY).atTime(this.time)
}

fun LocalDate.minusDays(days: Int): LocalDate {
    return this.minus(days, DateTimeUnit.DAY)
}

fun LocalDateTime.minusDays(days: Int): LocalDateTime {
    return this.date.minus(days, DateTimeUnit.DAY).atTime(this.time)
}

fun LocalTime.plusHours(hours: Int): LocalTime {
    val addedHours = this.hour + hours
    return LocalTime(addedHours, this.minute)
}

fun LocalTime.truncatedTo(): LocalTime {
    return LocalTime(this.hour, this.minute)
}

fun min(): LocalTime {
    return LocalTime(0, 0)
}

fun max(): LocalTime {
    return LocalTime(23, 59, 59, 999999999)
}

@JvmInline
value class SplitType private constructor(val value: Int) {
    companion object {
        val None = SplitType(0)
        val Start = SplitType(1)
        val End = SplitType(2)
        val Both = SplitType(3)
    }
}

data class PositionedTask(
    val task: Task,
    val splitType: SplitType,
    val date: LocalDate,
    val start: LocalTime,
    val end: LocalTime,
    val col: Int = 0,
    val colSpan: Int = 1,
    val colTotal: Int = 1,
)

sealed class ScheduleSize {
    class FixedSize(val size: Dp) : ScheduleSize()
    class FixedCount(val count: Float) : ScheduleSize()

    class Adaptive(val minSize: Dp) : ScheduleSize()
}

class TaskDataModifier(
    private val positionedTask: PositionedTask,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = positionedTask
}

fun Modifier.taskData(positionedTask: PositionedTask) = this.then(TaskDataModifier(positionedTask))

fun splitTasks(
    tasks: List<Task>,
    sessionTime: Int,
    shortBreakTime: Int,
    longBreakTime: Int,
): List<PositionedTask> {
    return tasks
        .map { task ->
            val end = task.start.calculateEndTime(
                focusSessions = task.focusSessions,
                sessionTime = sessionTime,
                shortBreakTime = shortBreakTime,
                longBreakTime = longBreakTime,
            )
            val startDate = task.start.date
            val endDate = end.date
            if (startDate == endDate) {
                listOf(
                    PositionedTask(
                        task,
                        SplitType.None,
                        task.start.date,
                        task.start.time,
                        end.time,
                    ),
                )
            } else {
                val days = differenceBetweenDays(startDate, endDate)
                val splitTasks = mutableListOf<PositionedTask>()
                for (i in 0..days) {
                    val date = startDate.plusDays(i)
                    splitTasks += PositionedTask(
                        task,
                        splitType = if (date == startDate) SplitType.End else if (date == endDate) SplitType.Start else SplitType.Both,
                        date = date,
                        start = if (date == startDate) {
                            task.start.time
                        } else {
                            min()
                        },
                        end = if (date == endDate) {
                            end.time
                        } else {
                            max()
                        },
                    )
                }
                splitTasks
            }
        }
        .flatten()
}

fun PositionedTask.overlapsWith(other: PositionedTask): Boolean {
    return date == other.date && start < other.end && end > other.start
}

fun List<PositionedTask>.timesOverlapWith(task: PositionedTask): Boolean {
    return any { it.overlapsWith(task) }
}

fun arrangeTasks(tasks: List<PositionedTask>): List<PositionedTask> {
    val positionedTasks = mutableListOf<PositionedTask>()
    val groupTasks: MutableList<MutableList<PositionedTask>> = mutableListOf()

    fun resetGroup() {
        groupTasks.forEachIndexed { columnIndex, column ->
            column.forEach { e ->
                positionedTasks.add(e.copy(col = columnIndex, colTotal = groupTasks.size))
            }
        }
        groupTasks.clear()
    }

    tasks.forEach { task ->
        var firstFreeCol = -1
        var numFreeCol = 0
        for (i in 0 until groupTasks.size) {
            val col = groupTasks[i]
            if (col.timesOverlapWith(task)) {
                if (firstFreeCol < 0) continue else break
            }
            if (firstFreeCol < 0) firstFreeCol = i
            numFreeCol++
        }

        when {
            // Overlaps with all, add a new column
            firstFreeCol < 0 -> {
                groupTasks += mutableListOf(task)
                // Expand anything that spans into the previous column and doesn't overlap with this task
                for (ci in 0 until groupTasks.size - 1) {
                    val col = groupTasks[ci]
                    col.forEachIndexed { ei, e ->
                        if (ci + e.colSpan == groupTasks.size - 1 && !e.overlapsWith(task)) {
                            col[ei] = e.copy(colSpan = e.colSpan + 1)
                        }
                    }
                }
            }
            // No overlap with any, start a new group
            numFreeCol == groupTasks.size -> {
                resetGroup()
                groupTasks += mutableListOf(task)
            }
            // At least one column free, add to first free column and expand to as many as possible
            else -> {
                groupTasks[firstFreeCol] += task.copy(colSpan = numFreeCol)
            }
        }
    }
    resetGroup()
    return positionedTasks
}

fun Long?.selectedDateMillisToLocalDateTime(): LocalDateTime {
    return Instant.fromEpochMilliseconds(this ?: 0)
        .toLocalDateTime(TimeZone.currentSystemDefault())
}

fun calculateFromFocusSessions(
    focusSessions: Int,
    sessionTime: Int = 25,
    shortBreakTime: Int = 5,
    longBreakTime: Int = 15,
    currentLocalDateTime: LocalDateTime,
): LocalTime {
    return if (focusSessions <= 0) {
        currentLocalDateTime.time
    } else {
        val totalSessionTimeMinutes = sessionTime * focusSessions
        val totalShortBreakTimeMinutes = shortBreakTime * (focusSessions - 1)
        val totalLongBreakTimeMinutes = longBreakTime * (focusSessions / 4)
        val totalBreakTimeMinutes = totalShortBreakTimeMinutes + totalLongBreakTimeMinutes
        val totalTaskTimeMinutes = totalSessionTimeMinutes + totalBreakTimeMinutes
        val totalTaskTimeMillis = totalTaskTimeMinutes.toEpochMilliseconds()
        val totalTaskTimeLocalDateTime =
            currentLocalDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
                .plus(totalTaskTimeMillis).selectedDateMillisToLocalDateTime()
        totalTaskTimeLocalDateTime.time
    }
}

fun Int.toEpochMilliseconds(): Long {
    return this * 60 * 1000L
}

fun LocalDateTime.dateTimeToString(): String {
    return this.toString()
}

fun toLocalDateTime(hour: Int, minute: Int, date: LocalDate): LocalDateTime {
    return LocalDateTime(
        date,
        LocalTime(hour, minute),
    )
}

fun String.isDigitsOnly(): Boolean {
    return all { it.isDigit() }
}

fun taskCompleteMessage(tasks: List<Task>): String {
    val completedTasks = tasks.filter { it.completed }.size
    return if (completedTasks == tasks.size && completedTasks != 0) {
        "Congrats! You've completed all your tasks for today"
    } else if (completedTasks == 0) {
        "You've not completed any tasks for today"
    } else if (taskCompletionPercentage(tasks) >= 90) {
        "Keep it up! You're almost done with your daily tasks"
    } else if (taskCompletionPercentage(tasks) >= 75) {
        "Wow!, Your daily tasks are almost done"
    } else if (taskCompletionPercentage(tasks) >= 50) {
        "You're halfway through your daily tasks"
    } else if (taskCompletionPercentage(tasks) >= 25) {
        "You're almost halfway through your daily tasks"
    } else if (taskCompletionPercentage(tasks) >= 10) {
        "You've completed a few tasks for today"
    } else {
        "You've completed $completedTasks tasks for today"
    }
}

fun taskCompletionPercentage(tasks: List<Task>): Int {
    val completedTasks = tasks.filter { it.completed }.size
    return if (completedTasks == 0) {
        0
    } else {
        (completedTasks.toFloat() / tasks.size.toFloat() * 100).toInt()
    }
}

fun String.taskColor(): Long {
    return taskTypes.find { it.name == this }?.color ?: 0xFFAFBBF2
}

fun String.taskIcon(): String {
    return taskTypes.find { it.name == this }?.icon ?: "other.xml"
}

fun getThisWeek(): List<LocalDate> {
    /**
     * From Monday to Sunday
     */
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val dayOfWeek = today.dayOfWeek.ordinal
    val startOfWeek = today.minus(dayOfWeek, DateTimeUnit.DAY)

    /**
     * Dates Between startOfWeek and endOfWeek inclusive
     */
    val dates = mutableListOf<LocalDate>()
    for (i in 0..6) {
        dates += startOfWeek.plus(i, DateTimeUnit.DAY)
    }
    return dates
}

fun getPreviousWeek(firstDateOfNextWeek: LocalDate = today().date): List<LocalDate> {
    /**
     * From Monday to Sunday
     */
    val startOfWeek = firstDateOfNextWeek.minus(7, DateTimeUnit.DAY)

    /**
     * Dates Between startOfWeek and endOfWeek inclusive
     */
    val dates = mutableListOf<LocalDate>()
    for (i in 0..6) {
        dates += startOfWeek.plus(i, DateTimeUnit.DAY)
    }
    return dates
}

/**
 * A function that will return the last 12 weeks
 * The list should be in descending order
 * The first item should be the current week - This week
 * The second item should be the previous week - Last week
 *
 * The format of the result should be like <String, List<LocalDate>>
 * For this week  -> <This Week, List<LocalDate>>
 * For the other weeks -> <Aug 1 - Aug 7, List<LocalDate>>
 * If a week is outside of this year then it should be <Dec 25, 2020 - Dec 31, 2020, List<LocalDate>>
 */
fun getLast52Weeks(): List<Pair<String, List<LocalDate>>> {
    val thisYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
    val thisWeek = getThisWeek()
    val previousWeek = getPreviousWeek(thisWeek.first())
    val weeks = mutableListOf<Pair<String, List<LocalDate>>>()
    weeks += "This Week" to thisWeek
    weeks += "Last Week" to previousWeek
    for (i in 0..51) {
        val week = getPreviousWeek(firstDateOfNextWeek = weeks.last().second.first())
        weeks += "${
        week.first().month.name.lowercase().capitalize(Locale.current).substring(
            0,
            3,
        )
        } ${week.first().dayOfMonth} ${if (week.first().year != thisYear) week.first().year else ""}" +
            " - ${
            week.last().month.name.lowercase().capitalize(Locale.current).substring(
                0,
                3,
            )
            } ${week.last().dayOfMonth} ${if (week.last().year != thisYear) week.last().year else ""}" to week
    }
    return weeks
}

fun List<Task>.completedTasks(dates: List<LocalDate>): List<Int> {
    return dates.map { date ->
        filter { task ->
            task.date.date == date
        }.size
    }
}

fun List<Float>.aAllEntriesAreZero(): Boolean {
    return all { it.toDouble() == 0.0 }
}

fun LocalDate.prettyFormat(): String {
    return "${this.dayOfMonth}${
    when (this.dayOfMonth) {
        1, 21, 31 -> "st"
        2, 22 -> "nd"
        3, 23 -> "rd"
        else -> "th"
    }
    }, ${this.month.name.lowercase().capitalize(Locale.current).substring(0, 3)} ${this.year}"
}

fun LocalDate.prettyPrintedMonthAndYear(): String {
    return "${this.month.name.lowercase().capitalize(Locale.current).substring(0, 3)} ${this.year}"
}

fun prettyTimeDifference(start: LocalDateTime, end: LocalDateTime, timeFormat: Int): String {
    return if (timeFormat == 12) {
        val startHourTo12HourSystem = if (start.hour > 12) {
            start.hour - 12
        } else {
            start.hour
        }
        val endHourTo12HourSystem = if (end.hour > 12) {
            end.hour - 12
        } else {
            end.hour
        }
        "$startHourTo12HourSystem:${start.minute.formattedZeroMinutes()} ${if (start.hour > 12) "PM" else "AM"} - ${
        endHourTo12HourSystem
        }:${end.minute.formattedZeroMinutes()} ${if (end.hour > 12) "PM" else "AM"}"
    } else {
        "${start.hour}:${start.minute.formattedZeroMinutes()} - ${end.hour}:${end.minute.formattedZeroMinutes()}"
    }
}

fun Int.formattedZeroMinutes(): String {
    return if (this < 10) {
        "0$this"
    } else {
        this.toString()
    }
}

fun Long.formattedZeroMinutes(): String {
    return if (this < 10) {
        "0$this"
    } else {
        this.toString()
    }
}

fun LocalTime.formattedTimeBasedOnTimeFormat(timeFormat: Int): String {
    return if (timeFormat == 12) {
        val hourTo12HourSystem = if (this.hour > 12) {
            this.hour - 12
        } else {
            this.hour
        }
        "$hourTo12HourSystem:${
        this.minute.formattedZeroMinutes()
        } ${if (this.hour > 12) "PM" else "AM"}"
    } else {
        "${this.hour}:${this.minute.formattedZeroMinutes()}"
    }
}

fun String.timeFormat(): Int {
    return if (this == "12-hour") {
        12
    } else {
        24
    }
}

fun Int.timeFormat(): String {
    return if (this == 12) {
        "12-hour"
    } else {
        "24-hour"
    }
}

fun Task.durationInMinutes(
    focusSessions: Int,
    sessionTime: Int,
    shortBreakTime: Int,
    longBreakTime: Int,
): Int {
    val end = start.calculateEndTime(
        focusSessions = focusSessions,
        sessionTime = sessionTime,
        shortBreakTime = shortBreakTime,
        longBreakTime = longBreakTime,
    )
    return (end.time.toSecondOfDay() - start.time.toSecondOfDay()) / 60
}

/**
 * LocalDates for List<LocalDate>
 * The last 1 year
 * The next 1 year
 */
fun calendarLocalDates(): List<LocalDate> {
    val thisYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
    val lastYear = thisYear - 1
    val nextYear = thisYear + 1
    val dates = mutableListOf<LocalDate>()
    for (i in 0..365) {
        dates += LocalDate(thisYear, 1, 1).plus(i, DateTimeUnit.DAY)
    }
    for (i in 0..365) {
        dates += LocalDate(lastYear, 1, 1).plus(i, DateTimeUnit.DAY)
    }
    for (i in 0..365) {
        dates += LocalDate(nextYear, 1, 1).plus(i, DateTimeUnit.DAY)
    }
    return dates
}

fun LocalDate.insideThisWeek(): Boolean {
    val thisWeek = getThisWeek()
    return this in thisWeek
}

fun today(): LocalDateTime {
    return Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
}

fun Long.toTimer(): String {
    /**
     * Input is in milliseconds
     */
    val seconds = this / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    return "${
    if (hours > 0) {
        hours.formattedZeroMinutes() + ":"
    } else {
        ""
    }
    }${(minutes - (hours * 60)).formattedZeroMinutes()}:${(seconds - (minutes * 60)).formattedZeroMinutes()}"
}

fun Long.toPercentage(total: Long): Float {
    /**
     * In increase order
     */
    return if (total == 0L) {
        0F
    } else {
        val perc = (100 - ((this.toFloat() / total.toFloat()) * 100))
        perc
    }
}

fun Long.toMinutes(): Int {
    return (this / 1000 / 60).toInt()
}

fun Int.toMillis(): Long {
    /**
     * Input is minutes
     */
    return (this * 60 * 1000).toLong()
}

fun String?.sessionType(): SessionType {
    return when (this) {
        "Focus" -> SessionType.Focus
        "ShortBreak" -> SessionType.ShortBreak
        "LongBreak" -> SessionType.LongBreak
        else -> SessionType.Focus
    }
}

val LocalAppNavigator: ProvidableCompositionLocal<Navigator?> = staticCompositionLocalOf { null }

@Composable
fun ProvideAppNavigator(navigator: Navigator, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAppNavigator provides navigator) {
        content()
    }
}

fun String.pickFirstName(): String {
    return this.split(" ").first()
}

fun LocalDateTime.calculateEndTime(
    focusSessions: Int,
    sessionTime: Int,
    shortBreakTime: Int,
    longBreakTime: Int,
): LocalDateTime {
    val totalSessionTimeMinutes = sessionTime * focusSessions
    val totalShortBreakTimeMinutes = shortBreakTime * (focusSessions - 1)
    val totalLongBreakTimeMinutes = longBreakTime * (focusSessions / 4)
    val totalBreakTimeMinutes = totalShortBreakTimeMinutes + totalLongBreakTimeMinutes
    val totalTaskTimeMinutes = totalSessionTimeMinutes + totalBreakTimeMinutes
    val totalTaskTimeMillis = totalTaskTimeMinutes.toEpochMilliseconds()
    return toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        .plus(totalTaskTimeMillis).selectedDateMillisToLocalDateTime()
}

fun Int.formattedNumber(): String {
    return "$this${
    when (this) {
        1, 21, 31 -> "st"
        2, 22 -> "nd"
        3, 23 -> "rd"
        else -> "th"
    }
    }"
}
