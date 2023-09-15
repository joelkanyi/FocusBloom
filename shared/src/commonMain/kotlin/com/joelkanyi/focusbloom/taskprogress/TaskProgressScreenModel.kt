package com.joelkanyi.focusbloom.taskprogress

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TaskProgressScreenModel(
    settingsRepository: SettingsRepository,
    private val tasksRepository: TasksRepository,
) : ScreenModel {
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

    fun updateConsumedFocusTime(taskId: Int, consumedTime: Long) {
        scope.launch {
            tasksRepository.updateConsumedFocusTime(taskId, consumedTime)
        }
    }

    private var job: Job? = null

    // private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val scope = coroutineScope

    private var sendCheckpoint = true

    private val _timerDuration = MutableStateFlow(0L)
    val timerDuration = _timerDuration.asStateFlow()
    fun setTimerDuration(duration: Long) {
        _timerDuration.value = duration
    }

    private val _time = MutableStateFlow(0L)
    val time: StateFlow<Long> = _time.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _state = MutableStateFlow<TimerState>(TimerState.Idle)
    val state: StateFlow<TimerState> = _state.asStateFlow()

    private val _event = Channel<TimerEvent>()
    val event: Flow<TimerEvent> = _event.receiveAsFlow()

    fun pause() {
        scope.launch {
            _state.emit(TimerState.Paused)
        }
    }

    fun resume() {
        scope.launch {
            _state.emit(TimerState.Ticking)
        }
    }

    fun stop() {
        scope.launch {
            _time.emit(0L)
            _duration.emit(0L)
            _state.emit(TimerState.Stopped)

            job?.cancel()
            job = null
        }
    }

    fun start(timerDuration: Long) {
        sendCheckpoint = true
        job?.cancel()

        job = scope.launch {
            _time.emit(timerDuration)
            _duration.emit(timerDuration)
            _state.emit(TimerState.Ticking)
            _event.trySend(TimerEvent.Started)

            while (_time.value > 0L) {
                delay(200L)

                if (_state.value == TimerState.Ticking) {
                    _time.emit(_time.value - 200L)
                    updateConsumedFocusTime(task.value?.id ?: 0, _time.value)
                }
            }

            finish()
        }
    }

    private suspend fun finish() {
        stop()
        _state.emit(TimerState.Finished)
        _event.trySend(TimerEvent.Finished)
    }

    fun reset() {
        scope.launch {
            stop()
            _state.emit(TimerState.Idle)
            _event.trySend(TimerEvent.Started)
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
    data object Started : TimerEvent()
    data object Checkpoint : TimerEvent()
    data object Finished : TimerEvent()
}
