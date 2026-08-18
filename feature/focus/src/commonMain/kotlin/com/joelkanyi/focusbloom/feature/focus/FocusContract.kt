package com.joelkanyi.focusbloom.feature.focus

import androidx.compose.runtime.Immutable
import com.joelkanyi.focusbloom.capability.session.SessionState
import com.joelkanyi.focusbloom.core.model.SessionPreset

/** The Focus screen is a pure function of this state. */
@Immutable
data class FocusUiState(
    val preset: SessionPreset = SessionPreset.Default,
    val session: SessionState = SessionState.Idle,
) {
    val isIdle: Boolean get() = session is SessionState.Idle
    val isFocusing: Boolean get() = session is SessionState.Focusing
    val isOnBreak: Boolean get() = session is SessionState.OnBreak
    val isParked: Boolean get() = session is SessionState.Parked
    val isCompleted: Boolean get() = session is SessionState.Completed

    /** True once focus time has passed the soft horizon: the timer is bending, not stopped. */
    val pastHorizon: Boolean get() = (session as? SessionState.Focusing)?.pastHorizon == true
}

sealed interface FocusUiEvent {
    data object StartSession : FocusUiEvent
    data object Park : FocusUiEvent
    data object Resume : FocusUiEvent
    data object TakeBreak : FocusUiEvent
    data object EndBreak : FocusUiEvent
    data object Finish : FocusUiEvent
}

sealed interface FocusUiEffect {
    data object SessionCompleted : FocusUiEffect
}
