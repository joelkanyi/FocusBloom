package com.joelkanyi.focusbloom.core.data.repository.tasks

import com.joelkanyi.focusbloom.core.data.mapper.toTask
import com.joelkanyi.focusbloom.core.data.mapper.toTaskEntity
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import com.joelkanyi.focusbloom.database.BloomDatabase
import com.joelkanyi.focusbloom.platform.DatabaseDriverFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TasksRepositoryImpl(
    private val databaseDriverFactory: DatabaseDriverFactory,
) : TasksRepository {
    private val appDatabase = BloomDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = appDatabase.taskQueries
    override fun getTasks(): Flow<List<Task>> {
        return flowOf(
            dbQuery
                .getAllTasks()
                .executeAsList()
                .map { it.toTask() },
        )
    }

    override fun getTask(id: Int): Flow<Task?> {
        return flowOf(
            dbQuery
                .getTaskById(id.toLong())
                .executeAsOneOrNull()
                ?.toTask(),
        )
    }

    override suspend fun insertTask(task: Task) {
        task.toTaskEntity().let {
            dbQuery.insertTask(
                id = it.id,
                name = it.name,
                description = it.description,
                start = it.start,
                end = it.end,
                color = it.color,
                current = it.current,
                date = it.date,
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
            )
        }
    }

    override suspend fun deleteTaskById(id: Int) {
        dbQuery.deleteTaskById(id.toLong())
    }

    override suspend fun deleteAllTasks() {
        dbQuery.deleteAllTasks()
    }
}
