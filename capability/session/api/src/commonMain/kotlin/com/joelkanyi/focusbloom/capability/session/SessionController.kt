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
import kotlinx.coroutines.flow.StateFlow

/**
 * Drives one focus session at a time. The UI observes [state] and sends intents. A break is
 * offered, never imposed ([beginBreak] is called by the user), and [park] is the "life happened"
 * escape that never fails a session.
 */
interface SessionController {
    val state: StateFlow<SessionState>

    fun start(preset: SessionPreset)

    /** Freezes the current focus into [SessionState.Parked]. */
    fun park()

    /** Continues a parked session from where it stopped. */
    fun resume()

    /** Moves from focus to a break (long or short). */
    fun beginBreak(long: Boolean)

    /** Ends the break, starting the next cycle or completing the session. */
    fun endBreak()

    /** Ends the whole session now, from any active state. */
    fun finish()
}
