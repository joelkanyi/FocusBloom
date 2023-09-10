package com.joelkanyi.focusbloom.core.domain.repository.tasks

import com.joelkanyi.focusbloom.core.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun getTasks(): Flow<List<Task>>
    fun getTask(id: Int): Flow<Task?>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTaskById(id: Int)
    suspend fun deleteAllTasks()
}
