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
package com.joelkanyi.focusbloom.feature.taskprogress

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.joelkanyi.focusbloom.core.domain.model.SessionType
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import com.joelkanyi.focusbloom.core.utils.formattedNumber
import com.joelkanyi.focusbloom.core.utils.sessionType
import com.joelkanyi.focusbloom.core.utils.toMillis
import com.joelkanyi.focusbloom.platform.NotificationsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskProgressScreenModel(
    private val settingsRepository: SettingsRepository,
    private val tasksRepository: TasksRepository,
    private val notificationManager: NotificationsManager,
) : ScreenModel {
    val shortBreakColor = settingsRepository.shortBreakColor()
        .map { it }
        .stateIn(
            screenModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val longBreakColor = settingsRepository.longBreakColor()
        .map { it }
        .stateIn(
            screenModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val focusColor = settingsRepository.focusColor()
        .map { it }
        .stateIn(
            screenModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val focusTime = settingsRepository.getSessionTime()
        .map {
            it?.toMillis() ?: (25).toMillis()
        }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
    val shortBreakTime = settingsRepository.getShortBreakTime()
        .map {
            it?.toMillis() ?: (5).toMillis()
        }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
    val longBreakTime = settingsRepository.getLongBreakTime()
        .map { it?.toMillis() ?: (15).toMillis() }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private val _remindersOn = MutableStateFlow<Boolean?>(null)
    private val remindersOn = _remindersOn.asStateFlow()
    fun getRemindersStatus() {
        screenModelScope.launch {
            settingsRepository.remindersOn().collectLatest {
                _remindersOn.value = it == 1
            }
        }
    }

    private val _task = MutableStateFlow<Task?>(null)
    val task = _task.asStateFlow()
    fun getTask(taskId: Int) {
        screenModelScope.launch {
            tasksRepository.getTask(taskId).collectLatest {
                _task.value = it
            }
        }
    }

    /**
     * This function updates the consumed focus time of the task
     * @param taskId the id of the task
     * @param consumedTime the consumed time of the focus
     */
    private fun updateConsumedFocusTime(taskId: Int, consumedTime: Long) {
        screenModelScope.launch {
            tasksRepository.updateConsumedFocusTime(taskId, consumedTime)
        }
    }

    /**
     * This function updates the consumed short break time of the task
     * @param taskId the id of the task
     * @param consumedTime the consumed time of the short break
     */
    private fun updateConsumedShortBreakTime(taskId: Int, consumedTime: Long) {
        screenModelScope.launch {
            tasksRepository.updateConsumedShortBreakTime(taskId, consumedTime)
        }
    }

    /**
     * This function updates the consumed long break time of the task
     * @param taskId the id of the task
     * @param consumedTime the consumed time of the long break
     */
    private fun updateConsumedLongBreakTime(taskId: Int, consumedTime: Long) {
        screenModelScope.launch {
            tasksRepository.updateConsumedLongBreakTime(taskId, consumedTime)
        }
    }

    /**
     * This function updates task as either in progress or not in progress
     * @param taskId the id of the task
     * @param inProgressTask the in progress task
     */
    private fun updateInProgressTask(taskId: Int, inProgressTask: Boolean) {
        screenModelScope.launch {
            tasksRepository.updateTaskInProgress(taskId, inProgressTask)
        }
    }

    fun updateActiveTask(taskId: Int, activeTask: Boolean) {
        screenModelScope.launch {
            tasksRepository.updateTaskActive(id = taskId, active = activeTask)
        }
    }

    /**
     * This function updates task as either completed or not completed
     * @param taskId the id of the task
     * @param completedTask the completed task
     */
    private fun updateCompletedTask(taskId: Int, completedTask: Boolean) {
        screenModelScope.launch {
            tasksRepository.updateTaskCompleted(taskId, completedTask)
        }
    }

    fun resetAllTasksToInactive() {
        screenModelScope.launch {
            tasksRepository.updateAllTasksActiveStatusToInactive()
        }
    }

    /**
     * This function updates the current cycle of the task
     * @param taskId the id of the task
     * @param currentCycle the current cycle of the task
     */
    private fun updateCurrentCycle(taskId: Int, currentCycle: Int) {
        screenModelScope.launch {
            tasksRepository.updateTaskCycleNumber(taskId, currentCycle)
        }
    }

    /**
     * This function updates the current session of the task (Focus, ShortBreak, LongBreak)
     * @param taskId the id of the task
     * @param currentSession the current session of the task
     */
    private fun updateCurrentSession(taskId: Int, currentSession: String) {
        screenModelScope.launch {
            tasksRepository.updateCurrentSessionName(taskId, currentSession)
        }
    }

    fun updateConsumedTime() {
        when (task.value?.current) {
            "Focus" -> updateConsumedFocusTime(
                task.value?.id ?: -1,
                Timer.tickingTime.value,
            )

            "ShortBreak" -> updateConsumedShortBreakTime(
                task.value?.id ?: -1,
                Timer.tickingTime.value,
            )

            "LongBreak" -> updateConsumedLongBreakTime(
                task.value?.id ?: -1,
                Timer.tickingTime.value,
            )
        }
    }

    fun executeTasks() {
        screenModelScope.launch {
            if (task.value?.currentCycle?.equals(0) == true) {
                println("executeTasks: first cycle")
                updateCurrentCycle(task.value?.id ?: 0, 1)
                updateCurrentSession(task.value?.id ?: 0, "Focus")
                updateInProgressTask(task.value?.id ?: 0, true)
                Timer.setTickingTime(focusTime.value ?: 0L)
                Timer.start(
                    update = {
                        updateConsumedTime()
                    },
                    executeTasks = {
                        executeTasks()
                    },
                )
            } else {
                when (task.value?.current) {
                    "Focus" -> {
                        if (task.value?.currentCycle == task.value?.focusSessions) {
                            if (remindersOn.value == true) {
                                notificationManager.showNotification(
                                    title = "[TASK] ${task.value?.name}",
                                    description = "${
                                    task.value?.currentCycle?.formattedNumber()
                                    } Focus Session Completed, going for a long break",
                                )
                            }

                            updateCurrentSession(task.value?.id ?: 0, "LongBreak")
                            updateInProgressTask(task.value?.id ?: 0, true)
                            Timer.setTickingTime(longBreakTime.value ?: 0L)
                            Timer.start(
                                update = {
                                    updateConsumedTime()
                                },
                                executeTasks = {
                                    executeTasks()
                                },
                            )
                        } else {
                            if (remindersOn.value == true) {
                                notificationManager.showNotification(
                                    title = "[TASK] ${task.value?.name}",
                                    description = "${task.value?.currentCycle?.formattedNumber()} focus session completed, going for a short break",
                                )
                            }

                            updateCurrentSession(task.value?.id ?: 0, "ShortBreak")
                            updateInProgressTask(task.value?.id ?: 0, true)
                            Timer.setTickingTime(shortBreakTime.value ?: 0L)
                            Timer.start(
                                update = {
                                    updateConsumedTime()
                                },
                                executeTasks = {
                                    executeTasks()
                                },
                            )
                        }
                    }

                    "ShortBreak" -> {
                        if (remindersOn.value == true) {
                            notificationManager.showNotification(
                                title = "[TASK] ${task.value?.name}",
                                description = "Short Break Completed, going for the ${
                                task.value?.currentCycle?.plus(
                                    1,
                                )?.formattedNumber()
                                } focus session",
                            )
                        }

                        updateCurrentSession(task.value?.id ?: 0, "Focus")
                        updateCurrentCycle(
                            task.value?.id ?: 0,
                            task.value?.currentCycle?.plus(1) ?: (0 + 1),
                        )
                        updateInProgressTask(task.value?.id ?: 0, true)
                        Timer.setTickingTime(focusTime.value ?: 0L)
                        Timer.start(
                            update = {
                                updateConsumedTime()
                            },
                            executeTasks = {
                                executeTasks()
                            },
                        )
                    }

                    "LongBreak" -> {
                        if (remindersOn.value == true) {
                            notificationManager.showNotification(
                                title = "[TASK] ${task.value?.name}",
                                description = "Good Job, you have completed this task \uD83C\uDF89\uD83C\uDF89",
                            )
                        }

                        val taskId = task.value?.id ?: 0
                        updateInProgressTask(taskId, false)
                        updateCompletedTask(taskId, true)
                        updateActiveTask(taskId, false)
                        Timer.stop()
                        Timer.reset()
                    }
                }
            }
        }
    }

    fun moveToNextSessionOfTheTask() {
        screenModelScope.launch {
            when (task.value?.current.sessionType()) {
                SessionType.Focus -> {
                    if (task.value?.currentCycle == task.value?.focusSessions) {
                        updateCurrentSession(task.value?.id ?: 0, "LongBreak")
                        updateInProgressTask(task.value?.id ?: 0, true)
                        Timer.setTickingTime(longBreakTime.value ?: 0L)
                        Timer.start(
                            update = {
                                updateConsumedTime()
                            },
                            executeTasks = {
                                executeTasks()
                            },
                        )
                    } else {
                        updateCurrentSession(task.value?.id ?: 0, "ShortBreak")
                        updateInProgressTask(task.value?.id ?: 0, true)
                        Timer.setTickingTime(shortBreakTime.value ?: 0L)
                        Timer.start(
                            update = {
                                updateConsumedTime()
                            },
                            executeTasks = {
                                executeTasks()
                            },
                        )
                    }
                }

                SessionType.LongBreak -> {
                    val taskId = task.value?.id ?: 0
                    updateInProgressTask(taskId, false)
                    updateCompletedTask(taskId, true)
                    updateActiveTask(taskId, false)
                    Timer.stop()
                    Timer.reset()
                }

                SessionType.ShortBreak -> {
                    updateCurrentSession(task.value?.id ?: 0, "Focus")
                    updateCurrentCycle(
                        task.value?.id ?: 0,
                        task.value?.currentCycle?.plus(1) ?: (0 + 1),
                    )
                    updateInProgressTask(task.value?.id ?: 0, true)
                    Timer.setTickingTime(focusTime.value ?: 0L)
                    Timer.start(
                        update = {
                            updateConsumedTime()
                        },
                        executeTasks = {
                            executeTasks()
                        },
                    )
                }
            }
        }
    }

    fun resetCurrentSessionOfTheTask() {
        screenModelScope.launch {
            when (task.value?.current.sessionType()) {
                SessionType.Focus -> {
                    updateCurrentSession(task.value?.id ?: 0, "Focus")
                    updateInProgressTask(task.value?.id ?: 0, true)
                    Timer.setTickingTime(focusTime.value ?: 0L)
                    Timer.start(
                        update = {
                            updateConsumedTime()
                        },
                        executeTasks = {
                            executeTasks()
                        },
                    )
                }

                SessionType.LongBreak -> {
                    val taskId = task.value?.id ?: 0
                    updateInProgressTask(taskId, false)
                    updateCompletedTask(taskId, true)
                    updateActiveTask(taskId, false)
                    Timer.stop()
                    Timer.reset()
                }

                SessionType.ShortBreak -> {
                    updateCurrentSession(task.value?.id ?: 0, "ShortBreak")
                    updateInProgressTask(task.value?.id ?: 0, true)
                    Timer.setTickingTime(shortBreakTime.value ?: 0L)
                    Timer.start(
                        update = {
                            updateConsumedTime()
                        },
                        executeTasks = {
                            executeTasks()
                        },
                    )
                }
            }
        }
    }
}
