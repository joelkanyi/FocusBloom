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
package com.joelkanyi.focusbloom.feature.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joelkanyi.focusbloom.capability.session.SessionController
import com.joelkanyi.focusbloom.capability.session.SessionState
import com.joelkanyi.focusbloom.core.model.SessionPreset
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class FocusViewModel(
    private val sessionController: SessionController,
) : ViewModel() {

    private val preset = MutableStateFlow(SessionPreset.Default)

    private val _uiEffect = Channel<FocusUiEffect>(Channel.BUFFERED)
    val uiEffect: Flow<FocusUiEffect> = _uiEffect.receiveAsFlow()

    val uiState: StateFlow<FocusUiState> =
        combine(preset, sessionController.state) { preset, session ->
            FocusUiState(preset = preset, session = session)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FocusUiState(),
        )

    init {
        sessionController.state
            .onEach { state ->
                if (state is SessionState.Completed) _uiEffect.trySend(FocusUiEffect.SessionCompleted)
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: FocusUiEvent) {
        when (event) {
            FocusUiEvent.StartSession -> sessionController.start(preset.value)
            FocusUiEvent.Park -> sessionController.park()
            FocusUiEvent.Resume -> sessionController.resume()
            FocusUiEvent.TakeBreak -> sessionController.beginBreak(long = false)
            FocusUiEvent.EndBreak -> sessionController.endBreak()
            FocusUiEvent.Finish -> sessionController.finish()
        }
    }
}
