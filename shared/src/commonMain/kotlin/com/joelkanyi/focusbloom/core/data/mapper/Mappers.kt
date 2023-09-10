package com.joelkanyi.focusbloom.core.data.mapper

import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.utils.dateTimeToString
import database.TaskEntity
import kotlinx.datetime.toLocalDateTime

fun TaskEntity.toTask() = Task(
    id = id.toInt(),
    name = name,
    description = description,
    start = start.toLocalDateTime(),
    end = end.toLocalDateTime(),
    color = color,
    current = current.toInt(),
    date = date.toLocalDateTime(),
)

fun Task.toTaskEntity() = TaskEntity(
    id = id.toLong(),
    name = name,
    description = description,
    start = start.dateTimeToString(),
    end = end.dateTimeToString(),
    color = color,
    current = current.toLong(),
    date = date.dateTimeToString(),
)
