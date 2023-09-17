package com.joelkanyi.focusbloom.taskprogress

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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TaskProgressScreenModel(
    settingsRepository: SettingsRepository,
    private val tasksRepository: TasksRepository,
) : ScreenModel {
    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow.asSharedFlow()

    private val _selectedTab = MutableStateFlow("Focus Time")
    val selectedTab = _selectedTab.asStateFlow()
    fun selectTab(index: String) {
        _selectedTab.value = index
    }

    private val _task = MutableStateFlow<Task?>(null)
    val task = _task.asStateFlow()
    fun getTask(taskId: Int) {
        scope.launch {
            println("TaskProgressScreenModel.getTask($taskId)")
            tasksRepository.getTask(taskId).collectLatest {
                _task.value = it
                if (it?.inProgressTask == true) {
                    val remainingTime = it.focusTime - it.consumedFocusTime
                    setTimerDuration(remainingTime)
                } else {
                    setTimerDuration(it?.focusTime ?: 0L)
                }
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

    private val _timerDuration = MutableStateFlow(0L)
    val timerDuration = _timerDuration.asStateFlow()
    private fun setTimerDuration(duration: Long) {
        _timerDuration.value = duration
    }

    private val _tickingTime = MutableStateFlow(0L)
    val tickingTime: StateFlow<Long> = _tickingTime.asStateFlow()

    private val _timerState = MutableStateFlow<TimerState>(TimerState.Idle)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    // private val _timerEvent = Channel<TimerEvent>()
    // val timerEvent: Flow<TimerEvent> = _timerEvent.receiveAsFlow()

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

    fun start(
        // timerDuration: Long,
        // currentSession: String,
        /*        totalCycles: Int,
                currentCycle: Int,
                isTaskInProgress: Boolean,*/
    ) {
        job?.cancel()

        job = scope.launch {
/*            if (task.value?.inProgressTask == true) {
                when (task.value?.current) {
                    *//*_tickingTime.emit(timerDuration)
_timerState.emit(TimerState.Ticking)
// _timerEvent.trySend(TimerEvent.Started)
_eventsFlow.emit(UiEvents.TimerEventStarted(timerDuration))*//*
                    "Focus" -> {
                        _tickingTime.emit(task.value?.consumedFocusTime ?: 0L)
                        _timerState.emit(TimerState.Ticking)
                        _eventsFlow.emit(
                            UiEvents.TimerEventStarted(
                                task.value?.consumedFocusTime ?: 0L,
                            ),
                        )
                    }

                    "ShortBreak" -> {
                        _tickingTime.emit(task.value?.consumedShortBreakTime ?: 0L)
                        _timerState.emit(TimerState.Ticking)
                        _eventsFlow.emit(
                            UiEvents.TimerEventStarted(
                                task.value?.consumedShortBreakTime ?: 0L,
                            ),
                        )
                    }

                    "LongBreak" -> {
                        _tickingTime.emit(task.value?.consumedLongBreakTime ?: 0L)
                        _timerState.emit(TimerState.Ticking)
                        _eventsFlow.emit(
                            UiEvents.TimerEventStarted(
                                task.value?.consumedLongBreakTime ?: 0L,
                            ),
                        )
                    }
                }
            } else {*/
            /*_tickingTime.emit(timerDuration)
            _timerState.emit(TimerState.Ticking)
            // _timerEvent.trySend(TimerEvent.Started)
            _eventsFlow.emit(UiEvents.TimerEventStarted(timerDuration))*/

            when (task.value?.current) {
                "Focus" -> {
                    _tickingTime.emit(task.value?.focusTime ?: 0L)
                    _timerState.emit(TimerState.Ticking)
                    _eventsFlow.emit(UiEvents.TimerEventStarted(task.value?.focusTime ?: 0L))
                }

                "ShortBreak" -> {
                    _tickingTime.emit(task.value?.shortBreakTime ?: 0L)
                    _timerState.emit(TimerState.Ticking)
                    _eventsFlow.emit(
                        UiEvents.TimerEventStarted(
                            task.value?.shortBreakTime ?: 0L,
                        ),
                    )
                }

                "LongBreak" -> {
                    _tickingTime.emit(task.value?.longBreakTime ?: 0L)
                    _timerState.emit(TimerState.Ticking)
                    _eventsFlow.emit(
                        UiEvents.TimerEventStarted(
                            task.value?.longBreakTime ?: 0L,
                        ),
                    )
                }
            }
            // }

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
            /**
             * This is how the algorithm should work:
             * - When a user opens a task, check if it is in progress
             *    - If it is in progress, continue
             *          - check the session (focus, short break, long break) that is in progress
             *          - set the timer duration to the remaining time of the session
             *          - start the timer
             *          - once the timer is completed, check if it is the last session
             *    - If it is not in progress, start a new session (focus)
             *      - take the focus time of the task
             *      - set the timer duration to the focus time
             *      - start the timer
             *      - once the timer is completed, check if it is the last session
             *  - A task has the focus cycles, so when a cycle is completed, check if it is the last cycle
             *      - if it is, then start a long break
             *          - once the long break is completed, show congratulations message that the task is completed
             *          - mark the task as completed
             *          - mark the task as not in progress
             *      - if it is not, then start a short break, once the short break is completed, start a new focus cycle (increment the consumed focus cycle counter)
             *      - repeat the above until the last focus cycle is completed
             */
            /**
             * TODO: check if it is the last cycle
             * TODO: if it is the last cycle, then show congratulations message that the task is completed
             */
            if (task.value?.currentCycle == task.value?.focusSessions) {
                updateCompletedTask(task.value?.id ?: 0, true)
                updateInProgressTask(task.value?.id ?: 0, false)
                stop()
                println("Congratulations! You have completed the task!")
                _eventsFlow.emit(UiEvents.ShowSnackbar("Congratulations! You have completed the task!"))
            } else {
                println("executeTasks: cycles not completed yet")
                when (task.value?.current) {
                    "Focus" -> {
                        println("executeTasks: going for a short break after a focus session")
                        updateCurrentSession(task.value?.id ?: 0, "ShortBreak")
                        updateCurrentCycle(
                            task.value?.id ?: 0,
                            task.value?.currentCycle?.plus(1) ?: (0 + 1),
                        )
                        updateInProgressTask(task.value?.id ?: 0, true)
                        start()
                    }

                    "ShortBreak" -> {
                        println("executeTasks: going for a focus session after a short break")
                        updateCurrentSession(task.value?.id ?: 0, "Focus")
                        updateInProgressTask(task.value?.id ?: 0, true)
                        start()
                    }

                    "LongBreak" -> {
                        println("executeTasks: going for a focus session after a long break")
                        updateCurrentSession(task.value?.id ?: 0, "Focus")
                        updateInProgressTask(task.value?.id ?: 0, true)
                        start()
                    }
                }
            }
        }
    }

    fun stop() {
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
            setTimerDuration(0L)
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

sealed class TimerEvent {
    data object Started : TimerEvent() // timer started
    data object Finished : TimerEvent() // timer finished
}
