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
import com.joelkanyi.focusbloom.core.testing.TestDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class SessionControllerTest {

    private fun preset(focusMinutes: Int = 1, cycles: Int = 2) =
        SessionPreset(focusMinutes = focusMinutes, shortBreakMinutes = 1, longBreakMinutes = 1, cycles = cycles)

    @Test
    fun crossesSoftHorizonWithoutStopping() = runTest {
        val controller = DefaultSessionController(TestDispatcherProvider(StandardTestDispatcher(testScheduler)))
        controller.start(preset(focusMinutes = 1))

        advanceTimeBy(30.seconds)
        runCurrent()
        val before = controller.state.value as SessionState.Focusing
        assertFalse(before.pastHorizon)

        advanceTimeBy(40.seconds) // total 70s, past the 60s horizon
        runCurrent()
        val after = controller.state.value as SessionState.Focusing
        assertTrue(after.pastHorizon, "session bends past the horizon instead of stopping")
        assertTrue(after.elapsed >= 60.seconds)
    }

    @Test
    fun parkFreezesElapsedAndResumeContinues() = runTest {
        val controller = DefaultSessionController(TestDispatcherProvider(StandardTestDispatcher(testScheduler)))
        controller.start(preset(focusMinutes = 5))

        advanceTimeBy(10.seconds)
        runCurrent()
        controller.park()
        val parked = controller.state.value as SessionState.Parked
        assertEquals(10.seconds, parked.focusedSoFar)

        advanceTimeBy(10.seconds) // time passes, but a parked session does not advance
        runCurrent()
        assertEquals(parked.focusedSoFar, (controller.state.value as SessionState.Parked).focusedSoFar)

        controller.resume()
        assertTrue(controller.state.value is SessionState.Focusing)
    }

    @Test
    fun breakThenNextCycleThenCompletes() = runTest {
        val controller = DefaultSessionController(TestDispatcherProvider(StandardTestDispatcher(testScheduler)))
        controller.start(preset(focusMinutes = 1, cycles = 2))

        controller.beginBreak(long = false)
        assertTrue(controller.state.value is SessionState.OnBreak)

        controller.endBreak() // cycle 1 -> 2
        assertEquals(2, (controller.state.value as SessionState.Focusing).cycle)

        controller.beginBreak(long = true)
        controller.endBreak() // cycle 2 was the last -> completed
        val done = controller.state.value as SessionState.Completed
        assertEquals(2, done.cyclesCompleted)
    }
}
