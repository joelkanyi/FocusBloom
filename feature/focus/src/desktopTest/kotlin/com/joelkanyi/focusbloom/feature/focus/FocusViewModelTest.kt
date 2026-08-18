package com.joelkanyi.focusbloom.feature.focus

import app.cash.turbine.test
import com.joelkanyi.focusbloom.capability.session.SessionController
import com.joelkanyi.focusbloom.capability.session.SessionState
import com.joelkanyi.focusbloom.core.model.SessionPreset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration

@OptIn(ExperimentalCoroutinesApi::class)
class FocusViewModelTest {

    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun startingASessionMovesStateToFocusing() = runTest {
        val controller = FakeSessionController()
        val viewModel = FocusViewModel(controller)

        viewModel.uiState.test {
            assertTrue(awaitItem().isIdle)
            viewModel.onEvent(FocusUiEvent.StartSession)
            assertTrue(awaitItem().isFocusing)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun completingASessionEmitsTheCompletedEffect() = runTest {
        val controller = FakeSessionController()
        val viewModel = FocusViewModel(controller)

        viewModel.uiEffect.test {
            controller.complete()
            assertEquals(FocusUiEffect.SessionCompleted, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}

private class FakeSessionController : SessionController {
    private val _state = MutableStateFlow<SessionState>(SessionState.Idle)
    override val state: StateFlow<SessionState> = _state.asStateFlow()

    override fun start(preset: SessionPreset) {
        _state.value = SessionState.Focusing(preset, cycle = 1, elapsed = Duration.ZERO, pastHorizon = false)
    }

    override fun park() = Unit
    override fun resume() = Unit
    override fun beginBreak(long: Boolean) = Unit
    override fun endBreak() = Unit
    override fun finish() {
        _state.value = SessionState.Completed(SessionPreset.Default, cyclesCompleted = 0)
    }

    fun complete() {
        _state.value = SessionState.Completed(SessionPreset.Default, cyclesCompleted = 1)
    }
}
