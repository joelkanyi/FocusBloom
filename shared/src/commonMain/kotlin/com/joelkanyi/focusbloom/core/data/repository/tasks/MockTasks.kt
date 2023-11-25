package com.joelkanyi.focusbloom.core.data.repository.tasks

import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import com.joelkanyi.focusbloom.core.utils.today
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockTasks : TasksRepository {
    private var tasks = mutableListOf(
        Task(
            id = 1,
            name = "Task 1",
            description = "Description 1",
            start = today(),
            color = 0,
            current = "Focus",
            date = today(),
            focusSessions = 0,
            completed = false,
            type = "Focus",
            consumedFocusTime = 0L,
            consumedShortBreakTime = 0L,
            consumedLongBreakTime = 0L,
            inProgressTask = false,
            currentCycle = 0,
            active = false,
        )
    )
    override fun getTasks(): Flow<List<Task>> {
        return flowOf(tasks)
    }

    override fun getTask(id: Int): Flow<Task?> {
        return flowOf(tasks.find { it.id == id })
    }

    override suspend fun addTask(task: Task) {
        tasks.add(task)
    }

    override suspend fun updateTask(task: Task) {
        tasks[tasks.indexOf(tasks.find { it.id == task.id })] = task
    }

    override suspend fun deleteTask(id: Int) {
        tasks.remove(tasks.find { it.id == id })
    }

    override suspend fun deleteAllTasks() {
        tasks.clear()
    }

    override suspend fun updateConsumedFocusTime(id: Int, focusTime: Long) {
        tasks[tasks.indexOf(tasks.find { it.id == id })].consumedFocusTime = focusTime
    }

    override suspend fun updateConsumedShortBreakTime(id: Int, shortBreakTime: Long) {
        tasks[tasks.indexOf(tasks.find { it.id == id })].consumedShortBreakTime = shortBreakTime
    }

    override suspend fun updateConsumedLongBreakTime(id: Int, longBreakTime: Long) {
        tasks[tasks.indexOf(tasks.find { it.id == id })].consumedLongBreakTime = longBreakTime
    }

    override suspend fun updateTaskInProgress(id: Int, inProgressTask: Boolean) {
        tasks[tasks.indexOf(tasks.find { it.id == id })].inProgressTask = inProgressTask
    }

    override suspend fun updateTaskCompleted(id: Int, completed: Boolean) {
        tasks[tasks.indexOf(tasks.find { it.id == id })].completed = completed
    }

    override suspend fun updateCurrentSessionName(id: Int, current: String) {
        tasks[tasks.indexOf(tasks.find { it.id == id })].current = current
    }

    override suspend fun updateTaskCycleNumber(id: Int, cycle: Int) {
        tasks[tasks.indexOf(tasks.find { it.id == id })].currentCycle = cycle
    }

    override fun getActiveTask(): Flow<Task?> {
        return flowOf(tasks.find { it.active })
    }

    override suspend fun updateTaskActive(id: Int, active: Boolean) {
        tasks[tasks.indexOf(tasks.find { it.id == id })].active = active
    }

    override suspend fun updateAllTasksActiveStatusToInactive() {
        tasks.forEach { it.active = false }
    }
}
