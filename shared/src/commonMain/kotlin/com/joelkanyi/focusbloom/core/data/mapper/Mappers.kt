package com.joelkanyi.focusbloom.core.data.mapper

import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.utils.dateTimeToString
import database.TaskEntity
import kotlinx.datetime.toLocalDateTime

fun TaskEntity.toTask() = Task(
    id = id,
    name = name,
    description = description,
    type = type,
    start = start.toLocalDateTime(),
    end = end.toLocalDateTime(),
    color = color,
    current = current,
    date = date.toLocalDateTime(),
    focusSessions = focusSessions,
    completed = completed,
    focusTime = focusTime,
    shortBreakTime = shortBreakTime,
    longBreakTime = longBreakTime,
    consumedFocusTime = consumedFocusTime,
    consumedShortBreakTime = consumedShortBreakTime,
    consumedLongBreakTime = consumedLongBreakTime,
    inProgressTask = inProgressTask,
    currentCycle = currentCycle,
)

fun Task.toTaskEntity() = TaskEntity(
    id = id,
    name = name,
    description = description,
    type = type,
    start = start.dateTimeToString(),
    end = end.dateTimeToString(),
    color = color,
    current = current,
    date = date.dateTimeToString(),
    focusSessions = focusSessions,
    completed = completed,
    focusTime = focusTime,
    shortBreakTime = shortBreakTime,
    longBreakTime = longBreakTime,
    consumedFocusTime = consumedFocusTime,
    consumedShortBreakTime = consumedShortBreakTime,
    consumedLongBreakTime = consumedLongBreakTime,
    inProgressTask = inProgressTask,
    currentCycle = currentCycle,
)
