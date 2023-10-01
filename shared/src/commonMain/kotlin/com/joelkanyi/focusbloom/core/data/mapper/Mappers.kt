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
    color = color,
    current = current,
    date = date.toLocalDateTime(),
    focusSessions = focusSessions,
    completed = completed,
    consumedFocusTime = consumedFocusTime,
    consumedShortBreakTime = consumedShortBreakTime,
    consumedLongBreakTime = consumedLongBreakTime,
    inProgressTask = inProgressTask,
    currentCycle = currentCycle,
    active = active,
)

fun Task.toTaskEntity() = TaskEntity(
    id = id,
    name = name,
    description = description,
    type = type,
    start = start.dateTimeToString(),
    color = color,
    current = current,
    date = date.dateTimeToString(),
    focusSessions = focusSessions,
    completed = completed,
    consumedFocusTime = consumedFocusTime,
    consumedShortBreakTime = consumedShortBreakTime,
    consumedLongBreakTime = consumedLongBreakTime,
    inProgressTask = inProgressTask,
    currentCycle = currentCycle,
    active = active,
)
