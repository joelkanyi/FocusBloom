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
    suspend fun updateCurrentSession(id: Int, current: String)
}
