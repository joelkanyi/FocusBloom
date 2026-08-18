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

import com.joelkanyi.focusbloom.core.model.SessionPreset
import kotlin.time.Duration

/**
 * The state of the Bending Timer. The soft horizon is not a hard cutoff: when focus time passes
 * the preset horizon, the session stays [Focusing] with [Focusing.pastHorizon] set, so overrun is
 * celebrated rather than stopped. [Parked] freezes a session without failing it; nothing resets.
 */
sealed interface SessionState {
    data object Idle : SessionState

    data class Focusing(
        val preset: SessionPreset,
        val cycle: Int,
        val elapsed: Duration,
        val pastHorizon: Boolean,
    ) : SessionState

    data class OnBreak(
        val preset: SessionPreset,
        val cycle: Int,
        val elapsed: Duration,
        val isLong: Boolean,
    ) : SessionState

    data class Parked(
        val preset: SessionPreset,
        val cycle: Int,
        val focusedSoFar: Duration,
    ) : SessionState

    data class Completed(
        val preset: SessionPreset,
        val cyclesCompleted: Int,
    ) : SessionState
}
