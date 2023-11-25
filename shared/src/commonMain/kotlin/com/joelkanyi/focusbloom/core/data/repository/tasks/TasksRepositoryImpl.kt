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
package com.joelkanyi.focusbloom.core.data.repository.tasks

import com.joelkanyi.focusbloom.core.data.local.sqldelight.DatabaseHelper
import com.joelkanyi.focusbloom.core.data.mapper.toTask
import com.joelkanyi.focusbloom.core.data.mapper.toTaskEntity
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TasksRepositoryImpl(
    private val databaseHelper: DatabaseHelper,
) : TasksRepository {
    override fun getTasks(): Flow<List<Task>> {
        return databaseHelper.queryAsListFlow {
            it.taskQueries.getAllTasks()
        }.map { taskEntities ->
            taskEntities.map { taskEntity ->
                taskEntity.toTask()
            }
        }
    }

    override fun getTask(id: Int): Flow<Task?> {
        return databaseHelper.queryAsOneOrNullFlow {
            it.taskQueries.getTaskById(id)
        }.map {
            it?.toTask()
        }
    }

    override suspend fun addTask(task: Task) {
        task.toTaskEntity().let { taskEntity ->
            databaseHelper.withDatabase {
                it.taskQueries.insertTask(
                    name = taskEntity.name,
                    description = taskEntity.description,
                    start = taskEntity.start,
                    color = taskEntity.color,
                    current = taskEntity.current,
                    date = taskEntity.date,
                    focusSessions = taskEntity.focusSessions,
                    completed = taskEntity.completed,
                    type = taskEntity.type,
                    consumedFocusTime = taskEntity.consumedFocusTime,
                    consumedShortBreakTime = taskEntity.consumedShortBreakTime,
                    consumedLongBreakTime = taskEntity.consumedLongBreakTime,
                    inProgressTask = taskEntity.inProgressTask,
                    currentCycle = taskEntity.currentCycle,
                    active = taskEntity.active,
                )
            }
        }
    }

    override suspend fun updateTask(task: Task) {
        task.toTaskEntity().let { taskEntity ->
            databaseHelper.withDatabase {
                it.taskQueries.updateTask(
                    id = taskEntity.id,
                    name = taskEntity.name,
                    description = taskEntity.description,
                    start = taskEntity.start,
                    color = taskEntity.color,
                    current = taskEntity.current,
                    date = taskEntity.date,
                    focusSessions = taskEntity.focusSessions,
                    completed = taskEntity.completed,
                    active = taskEntity.active,
                )
            }
        }
    }

    override suspend fun deleteTask(id: Int) {
        databaseHelper.withDatabase {
            it.taskQueries.deleteTaskById(id)
        }
    }

    override suspend fun deleteAllTasks() {
        databaseHelper.withDatabase {
            it.taskQueries.deleteAllTasks()
        }
    }

    override suspend fun updateConsumedFocusTime(id: Int, focusTime: Long) {
        databaseHelper.withDatabase {
            it.taskQueries.updateConsumedFocusTime(id = id, consumedFocusTime = focusTime)
        }
    }

    override suspend fun updateConsumedShortBreakTime(id: Int, shortBreakTime: Long) {
        databaseHelper.withDatabase {
            it.taskQueries.updateConsumedShortBreakTime(
                id = id,
                consumedShortBreakTime = shortBreakTime,
            )
        }
    }

    override suspend fun updateConsumedLongBreakTime(id: Int, longBreakTime: Long) {
        databaseHelper.withDatabase {
            it.taskQueries.updateConsumedLongBreakTime(
                id = id,
                consumedLongBreakTime = longBreakTime,
            )
        }
    }

    override suspend fun updateTaskInProgress(id: Int, inProgressTask: Boolean) {
        databaseHelper.withDatabase {
            it.taskQueries.updateInProgressTask(id = id, inProgressTask = inProgressTask)
        }
    }

    override suspend fun updateTaskCompleted(id: Int, completed: Boolean) {
        databaseHelper.withDatabase {
            it.taskQueries.updateTaskCompleted(id = id, completed = completed)
        }
    }

    override suspend fun updateCurrentSessionName(id: Int, current: String) {
        databaseHelper.withDatabase {
            it.taskQueries.updateCurrentSessionName(id = id, current = current)
        }
    }

    override suspend fun updateTaskCycleNumber(id: Int, cycle: Int) {
        databaseHelper.withDatabase {
            it.taskQueries.updateTaskCycleNumber(id = id, currentCycle = cycle)
        }
    }

    override fun getActiveTask(): Flow<Task?> {
        return databaseHelper.queryAsOneOrNullFlow {
            it.taskQueries.getActiveTask()
        }.map {
            it?.toTask()
        }
    }

    override suspend fun updateTaskActive(id: Int, active: Boolean) {
        databaseHelper.withDatabase {
            it.taskQueries.updateTaskActiveStatus(id = id, active = active)
        }
    }

    override suspend fun updateAllTasksActiveStatusToInactive() {
        databaseHelper.withDatabase {
            it.taskQueries.updateAllTasksActiveStatusToInactive()
        }
    }
}
