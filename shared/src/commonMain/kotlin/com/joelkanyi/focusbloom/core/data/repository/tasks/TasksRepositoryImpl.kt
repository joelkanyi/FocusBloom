package com.joelkanyi.focusbloom.core.data.repository.tasks

import com.joelkanyi.focusbloom.core.data.mapper.toTask
import com.joelkanyi.focusbloom.core.data.mapper.toTaskEntity
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import com.joelkanyi.focusbloom.database.BloomDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneNotNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TasksRepositoryImpl(
    bloomDatabase: BloomDatabase,
) : TasksRepository {
    private val dbQuery = bloomDatabase.taskQueries
    override fun getTasks(): Flow<List<Task>> {
        return dbQuery
            .getAllTasks()
            .asFlow()
            .mapToList()
            .map { tasks ->
                tasks.map {
                    it.toTask()
                }
            }
    }

    override fun getTask(id: Int): Flow<Task?> {
        return dbQuery
            .getTaskById(id)
            .asFlow()
            .mapToOneNotNull()
            .map { taskEntity ->
                taskEntity.toTask()
            }
    }

    override suspend fun addTask(task: Task) {
        task.toTaskEntity().let {
            dbQuery.insertTask(
                name = it.name,
                description = it.description,
                start = it.start,
                end = it.end,
                color = it.color,
                current = it.current,
                date = it.date,
                focusSessions = it.focusSessions,
                completed = it.completed,
                focusTime = it.focusTime,
                shortBreakTime = it.shortBreakTime,
                longBreakTime = it.longBreakTime,
                type = it.type,
                consumedFocusTime = it.consumedFocusTime,
                consumedShortBreakTime = it.consumedShortBreakTime,
                consumedLongBreakTime = it.consumedLongBreakTime,
                inProgressTask = it.inProgressTask,
                currentCycle = it.currentCycle,
            )
        }
    }

    override suspend fun updateTask(task: Task) {
        task.toTaskEntity().let {
            dbQuery.updateTask(
                id = it.id,
                name = it.name,
                description = it.description,
                start = it.start,
                end = it.end,
                color = it.color,
                current = it.current,
                date = it.date,
                focusSessions = it.focusSessions,
                completed = it.completed,
                focusTime = it.focusTime,
                shortBreakTime = it.shortBreakTime,
                longBreakTime = it.longBreakTime,
            )
        }
    }

    override suspend fun deleteTask(id: Int) {
        dbQuery.deleteTaskById(id)
    }

    override suspend fun deleteAllTasks() {
        dbQuery.deleteAllTasks()
    }

    override suspend fun updateConsumedFocusTime(id: Int, focusTime: Long) {
        dbQuery.updateConsumedFocusTime(id = id, consumedFocusTime = focusTime)
    }

    override suspend fun updateConsumedShortBreakTime(id: Int, shortBreakTime: Long) {
        dbQuery.updateConsumedShortBreakTime(id = id, consumedShortBreakTime = shortBreakTime)
    }

    override suspend fun updateConsumedLongBreakTime(id: Int, longBreakTime: Long) {
        dbQuery.updateConsumedLongBreakTime(id = id, consumedLongBreakTime = longBreakTime)
    }

    override suspend fun updateTaskInProgress(id: Int, inProgressTask: Boolean) {
        dbQuery.updateInProgressTask(id = id, inProgressTask = inProgressTask)
    }

    override suspend fun updateTaskCompleted(id: Int, completed: Boolean) {
        dbQuery.updateTaskCompleted(id = id, completed = completed)
    }

    override suspend fun updateCurrentSessionName(id: Int, current: String) {
        dbQuery.updateCurrentSessionName(id = id, current = current)
    }

    override suspend fun updateTaskCycleNumber(id: Int, cycle: Int) {
        dbQuery.updateTaskCycleNumber(id = id, currentCycle = cycle)
    }
}
