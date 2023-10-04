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

import com.joelkanyi.focusbloom.core.utils.UiEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object Timer {
    private val _eventsFlow = Channel<UiEvents>(Channel.UNLIMITED)
    val eventsFlow = _eventsFlow.receiveAsFlow()

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _tickingTime = MutableStateFlow(0L)
    val tickingTime: StateFlow<Long> = _tickingTime.asStateFlow()
    fun setTickingTime(time: Long) {
        _tickingTime.value = time
    }

    private val _timerState = MutableStateFlow<TimerState>(TimerState.Idle)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()
    private fun setTimerState(state: TimerState) {
        _timerState.value = state
    }

    private val _timerEvent = MutableStateFlow<TimerEvent>(TimerEvent.Idle)
    private val timerEvent: StateFlow<TimerEvent> = _timerEvent.asStateFlow()

    fun stop() {
        job?.cancel()
        _timerState.value = TimerState.Stopped
    }

    private fun finish(executeTasks: () -> Unit) {
        scope.launch {
            stop()
            _timerState.emit(TimerState.Finished)
            _timerEvent.emit(TimerEvent.TimerEventFinished)
            if (timerEvent.value == TimerEvent.TimerEventFinished) {
                executeTasks()
            }
        }
    }

    fun reset() {
        scope.launch {
            stop()
            _timerState.emit(TimerState.Idle)
            _timerEvent.emit(TimerEvent.TimerEventStarted)
        }
    }

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

    fun start(update: () -> Unit, executeTasks: () -> Unit) {
        job?.cancel()
        job = scope.launch {
            setTimerState(TimerState.Ticking)
            _timerEvent.emit(TimerEvent.TimerEventStarted)

            while (tickingTime.value > 0L) {
                delay(200L)

                if (timerState.value == TimerState.Ticking) {
                    setTickingTime(tickingTime.value - 200L)
                    update()
                }
            }

            finish(executeTasks)
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
    data object TimerEventStarted : TimerEvent()
    data object TimerEventFinished : TimerEvent()
    data object Idle : TimerEvent()
}
