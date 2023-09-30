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
package com.joelkanyi.focusbloom.core.domain.repository.tasks

import com.joelkanyi.focusbloom.core.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun getTasks(): Flow<List<Task>>
    fun getTask(id: Int): Flow<Task?>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(id: Int)
    suspend fun deleteAllTasks()
    suspend fun updateConsumedFocusTime(id: Int, focusTime: Long)
    suspend fun updateConsumedShortBreakTime(id: Int, shortBreakTime: Long)
    suspend fun updateConsumedLongBreakTime(id: Int, longBreakTime: Long)
    suspend fun updateTaskInProgress(id: Int, inProgressTask: Boolean)
    suspend fun updateTaskCompleted(id: Int, completed: Boolean)
    suspend fun updateCurrentSessionName(id: Int, current: String)
    suspend fun updateTaskCycleNumber(id: Int, cycle: Int)
    fun getActiveTask(): Flow<Task?>
    suspend fun updateTaskActive(id: Int, active: Boolean)

    suspend fun updateAllTasksActiveStatusToInactive()
}
