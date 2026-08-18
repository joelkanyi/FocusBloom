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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joelkanyi.focusbloom.capability.session.SessionState
import com.joelkanyi.focusbloom.core.designsystem.component.FocusBloomTimerRing
import com.joelkanyi.focusbloom.core.designsystem.util.ObserveAsEvents
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
import io.github.joelkanyi.jenga.component.layout.JengaBox
import io.github.joelkanyi.jenga.component.layout.JengaInline
import io.github.joelkanyi.jenga.component.layout.JengaStack
import io.github.joelkanyi.jenga.component.text.JengaText
import io.github.joelkanyi.jenga.theme.JengaTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun FocusScreen(
    viewModel: FocusViewModel,
    onSessionCompleted: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveAsEvents(viewModel.uiEffect) { effect ->
        when (effect) {
            FocusUiEffect.SessionCompleted -> onSessionCompleted()
        }
    }
    FocusScreenContent(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun FocusScreenContent(
    state: FocusUiState,
    onEvent: (FocusUiEvent) -> Unit,
) {
    JengaBox(
        modifier = Modifier.fillMaxSize(),
        background = JengaTheme.colors.background,
        padding = PaddingValues(JengaTheme.spacing.xl),
        contentAlignment = Alignment.Center,
    ) {
        JengaStack(
            space = JengaTheme.spacing.xl,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (val session = state.session) {
                is SessionState.Idle -> {
                    JengaText(
                        text = "Ready to focus?",
                        style = JengaTheme.typography.headingMedium,
                        color = JengaTheme.colors.textPrimary,
                    )
                    JengaButton(
                        text = "Start ${state.preset.focusMinutes} minutes",
                        onClick = { onEvent(FocusUiEvent.StartSession) },
                        variant = JengaButtonVariant.Primary,
                    )
                }

                is SessionState.Focusing -> {
                    val horizon = state.preset.focusMinutes.minutes
                    val fraction = if (horizon > Duration.ZERO) {
                        (session.elapsed / horizon).toFloat()
                    } else {
                        0f
                    }
                    JengaBox(contentAlignment = Alignment.Center) {
                        FocusBloomTimerRing(
                            fraction = fraction,
                            bloom = session.pastHorizon,
                        )
                        JengaStack(horizontalAlignment = Alignment.CenterHorizontally) {
                            JengaText(
                                text = session.elapsed.asClock(),
                                style = JengaTheme.typography.display,
                                color = JengaTheme.colors.textPrimary,
                            )
                            if (session.pastHorizon) {
                                JengaText(
                                    text = "in flow, keep going",
                                    style = JengaTheme.typography.bodyMedium,
                                    color = JengaTheme.colors.brand,
                                )
                            }
                        }
                    }
                    JengaInline(space = JengaTheme.spacing.md) {
                        JengaButton(
                            text = "Life happened",
                            onClick = { onEvent(FocusUiEvent.Park) },
                            variant = JengaButtonVariant.Ghost,
                        )
                        JengaButton(
                            text = "Break",
                            onClick = { onEvent(FocusUiEvent.TakeBreak) },
                            variant = JengaButtonVariant.Neutral,
                        )
                        JengaButton(
                            text = "Finish",
                            onClick = { onEvent(FocusUiEvent.Finish) },
                            variant = JengaButtonVariant.Ink,
                        )
                    }
                }

                is SessionState.OnBreak -> {
                    JengaText(
                        text = if (session.isLong) "Long break" else "Short break",
                        style = JengaTheme.typography.headingMedium,
                        color = JengaTheme.colors.textPrimary,
                    )
                    JengaText(
                        text = session.elapsed.asClock(),
                        style = JengaTheme.typography.display,
                        color = JengaTheme.colors.textMuted,
                    )
                    JengaButton(
                        text = "Back to focus",
                        onClick = { onEvent(FocusUiEvent.EndBreak) },
                        variant = JengaButtonVariant.Primary,
                    )
                }

                is SessionState.Parked -> {
                    JengaText(
                        text = "Parked",
                        style = JengaTheme.typography.headingMedium,
                        color = JengaTheme.colors.textPrimary,
                    )
                    JengaText(
                        text = session.focusedSoFar.asClock(),
                        style = JengaTheme.typography.display,
                        color = JengaTheme.colors.textMuted,
                    )
                    JengaInline(space = JengaTheme.spacing.md) {
                        JengaButton(
                            text = "Resume",
                            onClick = { onEvent(FocusUiEvent.Resume) },
                            variant = JengaButtonVariant.Primary,
                        )
                        JengaButton(
                            text = "Finish",
                            onClick = { onEvent(FocusUiEvent.Finish) },
                            variant = JengaButtonVariant.Ghost,
                        )
                    }
                }

                is SessionState.Completed -> {
                    JengaText(
                        text = "Nice work",
                        style = JengaTheme.typography.headingLarge,
                        color = JengaTheme.colors.textPrimary,
                    )
                    JengaText(
                        text = "${session.cyclesCompleted} cycles done",
                        style = JengaTheme.typography.bodyMedium,
                        color = JengaTheme.colors.textMuted,
                    )
                    JengaButton(
                        text = "Start another",
                        onClick = { onEvent(FocusUiEvent.StartSession) },
                        variant = JengaButtonVariant.Primary,
                    )
                }
            }
        }
    }
}

private fun Duration.asClock(): String {
    val totalSeconds = inWholeSeconds
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}
