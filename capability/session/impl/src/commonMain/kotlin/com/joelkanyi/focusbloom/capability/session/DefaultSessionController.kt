/*
 * Copyright 2026 Joel Kanyi.
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
package com.joelkanyi.focusbloom.capability.session

import com.joelkanyi.focusbloom.core.common.DispatcherProvider
import com.joelkanyi.focusbloom.core.model.SessionPreset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * The Bending Timer engine. Elapsed time advances one [tick] at a time (so it is trivially
 * testable on virtual time), and crossing the horizon only flips [SessionState.Focusing.pastHorizon]
 * rather than stopping. Owns its own scope; a single instance drives the one active session.
 */
class DefaultSessionController(
    dispatchers: DispatcherProvider,
    private val tick: Duration = 1.seconds,
) : SessionController {

    private val scope = CoroutineScope(dispatchers.default + SupervisorJob())
    private val _state = MutableStateFlow<SessionState>(SessionState.Idle)
    override val state: StateFlow<SessionState> = _state.asStateFlow()

    private var ticker: Job? = null

    override fun start(preset: SessionPreset) {
        _state.value = SessionState.Focusing(preset, cycle = 1, elapsed = Duration.ZERO, pastHorizon = false)
        restartTicker()
    }

    override fun park() {
        (_state.value as? SessionState.Focusing)?.let { s ->
            ticker?.cancel()
            _state.value = SessionState.Parked(s.preset, s.cycle, s.elapsed)
        }
    }

    override fun resume() {
        (_state.value as? SessionState.Parked)?.let { s ->
            _state.value = SessionState.Focusing(
                preset = s.preset,
                cycle = s.cycle,
                elapsed = s.focusedSoFar,
                pastHorizon = s.focusedSoFar >= s.preset.focusMinutes.minutes,
            )
            restartTicker()
        }
    }

    override fun beginBreak(long: Boolean) {
        (_state.value as? SessionState.Focusing)?.let { s ->
            _state.value = SessionState.OnBreak(s.preset, s.cycle, Duration.ZERO, long)
            restartTicker()
        }
    }

    override fun endBreak() {
        (_state.value as? SessionState.OnBreak)?.let { s ->
            val next = s.cycle + 1
            if (next > s.preset.cycles) {
                ticker?.cancel()
                _state.value = SessionState.Completed(s.preset, s.preset.cycles)
            } else {
                _state.value = SessionState.Focusing(s.preset, next, Duration.ZERO, pastHorizon = false)
                restartTicker()
            }
        }
    }

    override fun finish() {
        ticker?.cancel()
        val current = _state.value
        val preset = current.presetOrNull() ?: return
        _state.value = SessionState.Completed(preset, current.cycleOrZero())
    }

    private fun restartTicker() {
        ticker?.cancel()
        ticker = scope.launch {
            while (isActive) {
                delay(tick)
                onTick()
            }
        }
    }

    private fun onTick() {
        when (val s = _state.value) {
            is SessionState.Focusing -> {
                val elapsed = s.elapsed + tick
                _state.value = s.copy(
                    elapsed = elapsed,
                    pastHorizon = elapsed >= s.preset.focusMinutes.minutes,
                )
            }

            is SessionState.OnBreak -> _state.value = s.copy(elapsed = s.elapsed + tick)
            else -> Unit
        }
    }
}

private fun SessionState.presetOrNull(): SessionPreset? = when (this) {
    is SessionState.Focusing -> preset
    is SessionState.OnBreak -> preset
    is SessionState.Parked -> preset
    else -> null
}

private fun SessionState.cycleOrZero(): Int = when (this) {
    is SessionState.Focusing -> cycle
    is SessionState.OnBreak -> cycle
    is SessionState.Parked -> cycle
    else -> 0
}
