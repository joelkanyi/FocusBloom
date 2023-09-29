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
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import com.joelkanyi.focusbloom.core.utils.UiEvents
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskProgressScreenModel(
    settingsRepository: SettingsRepository,
    private val tasksRepository: TasksRepository,
) : ScreenModel {
    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow.asSharedFlow()

    val shortBreakColor = settingsRepository.shortBreakColor()
        .map { it }
        .stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val longBreakColor = settingsRepository.longBreakColor()
        .map { it }
        .stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val focusColor = settingsRepository.focusColor()
        .map { it }
        .stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val _task = MutableStateFlow<Task?>(null)
    val task = _task.asStateFlow()
    fun getTask(taskId: Int) {
        scope.launch {
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
        scope.launch {
            tasksRepository.updateConsumedFocusTime(taskId, consumedTime)
        }
    }

    /**
     * This function updates the consumed short break time of the task
     * @param taskId the id of the task
     * @param consumedTime the consumed time of the short break
     */
    private fun updateConsumedShortBreakTime(taskId: Int, consumedTime: Long) {
        scope.launch {
            tasksRepository.updateConsumedShortBreakTime(taskId, consumedTime)
        }
    }

    /**
     * This function updates the consumed long break time of the task
     * @param taskId the id of the task
     * @param consumedTime the consumed time of the long break
     */
    private fun updateConsumedLongBreakTime(taskId: Int, consumedTime: Long) {
        scope.launch {
            tasksRepository.updateConsumedLongBreakTime(taskId, consumedTime)
        }
    }

    /**
     * This function updates task as either in progress or not in progress
     * @param taskId the id of the task
     * @param inProgressTask the in progress task
     */
    private fun updateInProgressTask(taskId: Int, inProgressTask: Boolean) {
        scope.launch {
            tasksRepository.updateTaskInProgress(taskId, inProgressTask)
        }
    }

    /**
     * This function updates task as either completed or not completed
     * @param taskId the id of the task
     * @param completedTask the completed task
     */
    private fun updateCompletedTask(taskId: Int, completedTask: Boolean) {
        scope.launch {
            tasksRepository.updateTaskCompleted(taskId, completedTask)
        }
    }

    /**
     * This function updates the current cycle of the task
     * @param taskId the id of the task
     * @param currentCycle the current cycle of the task
     */
    private fun updateCurrentCycle(taskId: Int, currentCycle: Int) {
        scope.launch {
            tasksRepository.updateTaskCycleNumber(taskId, currentCycle)
        }
    }

    /**
     * This function updates the current session of the task (Focus, ShortBreak, LongBreak)
     * @param taskId the id of the task
     * @param currentSession the current session of the task
     */
    private fun updateCurrentSession(taskId: Int, currentSession: String) {
        scope.launch {
            tasksRepository.updateCurrentSessionName(taskId, currentSession)
        }
    }

    private var job: Job? = null
    private val scope = coroutineScope

    private val _tickingTime = MutableStateFlow(0L)
    val tickingTime: StateFlow<Long> = _tickingTime.asStateFlow()

    private val _timerState = MutableStateFlow<TimerState>(TimerState.Idle)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    fun pause() {
        scope.launch {
            _timerState.emit(TimerState.Paused)
        }
    }

    fun resume() {
        scope.launch {
            _timerState.emit(TimerState.Ticking)
        }
    }

    fun start() {
        job?.cancel()
        job = scope.launch {
            when (task.value?.current) {
                "Focus" -> {
                    _timerState.emit(TimerState.Ticking)
                    _eventsFlow.emit(UiEvents.TimerEventStarted(task.value?.focusTime ?: 0L))
                }

                "ShortBreak" -> {
                    _timerState.emit(TimerState.Ticking)
                    _eventsFlow.emit(
                        UiEvents.TimerEventStarted(
                            task.value?.shortBreakTime ?: 0L,
                        ),
                    )
                }

                "LongBreak" -> {
                    _timerState.emit(TimerState.Ticking)
                    _eventsFlow.emit(
                        UiEvents.TimerEventStarted(
                            task.value?.longBreakTime ?: 0L,
                        ),
                    )
                }
            }

            while (_tickingTime.value > 0L) {
                delay(200L)

                if (_timerState.value == TimerState.Ticking) {
                    _tickingTime.emit(_tickingTime.value - 200L)

                    when (task.value?.current) {
                        "Focus" -> updateConsumedFocusTime(task.value?.id ?: 0, _tickingTime.value)
                        "ShortBreak" -> updateConsumedShortBreakTime(
                            task.value?.id ?: 0,
                            _tickingTime.value,
                        )

                        "LongBreak" -> updateConsumedLongBreakTime(
                            task.value?.id ?: 0,
                            _tickingTime.value,
                        )
                    }
                }
            }

            finish()
        }
    }

    fun executeTasks() {
        scope.launch {
            println("executeTasks: cycles not completed")
            if (task.value?.currentCycle?.equals(0) == true) {
                println("executeTasks: first cycle")
                updateCurrentCycle(task.value?.id ?: 0, 1)
                updateCurrentSession(task.value?.id ?: 0, "Focus")
                updateInProgressTask(task.value?.id ?: 0, true)
                _tickingTime.emit(task.value?.focusTime ?: 0L)
                start()
            } else {
                when (task.value?.current) {
                    "Focus" -> {
                        if (task.value?.currentCycle == task.value?.focusSessions) {
                            println("executeTasks: going for a long break after a focus session")
                            updateCurrentSession(task.value?.id ?: 0, "LongBreak")
                            updateInProgressTask(task.value?.id ?: 0, true)
                            _tickingTime.emit(task.value?.longBreakTime ?: 0L)
                            start()
                        } else {
                            println("executeTasks: going for a short break after a focus session")
                            updateCurrentSession(task.value?.id ?: 0, "ShortBreak")
                            updateInProgressTask(task.value?.id ?: 0, true)
                            _tickingTime.emit(task.value?.shortBreakTime ?: 0L)
                            start()
                        }
                    }

                    "ShortBreak" -> {
                        println("executeTasks: going for a focus session after a short break")
                        updateCurrentSession(task.value?.id ?: 0, "Focus")
                        updateCurrentCycle(
                            task.value?.id ?: 0,
                            task.value?.currentCycle?.plus(1) ?: (0 + 1),
                        )
                        updateInProgressTask(task.value?.id ?: 0, true)
                        _tickingTime.emit(task.value?.focusTime ?: 0L)
                        start()
                    }

                    "LongBreak" -> {
                        println("executeTasks: completed all cycles")
                        updateCompletedTask(task.value?.id ?: 0, true)
                        updateInProgressTask(task.value?.id ?: 0, false)
                        stop()
                        println("Congratulations! You have completed the task!")
                        _eventsFlow.emit(UiEvents.ShowSnackbar("Congratulations! You have completed the task!"))
                    }
                }
            }
        }
    }

    private fun stop() {
        job?.cancel()
        _timerState.value = TimerState.Stopped
    }

    private suspend fun finish() {
        scope.launch {
            stop()
            _timerState.emit(TimerState.Finished)
            _eventsFlow.emit(UiEvents.TimerEventFinished)
        }
    }

    fun reset() {
        scope.launch {
            stop()
            _timerState.emit(TimerState.Idle)
            _eventsFlow.emit(UiEvents.TimerEventStarted(0L))
        }
    }

    fun next() {
        scope.launch {
        }
    }
}

sealed class TimerState {
    data object Idle : TimerState() // timer ready to start
    data object Ticking : TimerState() // timer is ticking
    data object Paused : TimerState() // timer in paused state
    data object Finished : TimerState() // timer finished
    data object Stopped : TimerState() // timer stopped programmatically
}
