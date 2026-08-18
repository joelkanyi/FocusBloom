package com.joelkanyi.focusbloom.feature.focus

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joelkanyi.focusbloom.capability.session.SessionState
import com.joelkanyi.focusbloom.core.designsystem.util.ObserveAsEvents
import io.github.joelkanyi.jenga.component.button.JengaButton
import io.github.joelkanyi.jenga.component.button.JengaButtonVariant
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(JengaTheme.colors.background)
            .padding(JengaTheme.spacing.xl),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(JengaTheme.spacing.xl),
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
                    Box(contentAlignment = Alignment.Center) {
                        TimerRing(
                            fraction = fraction,
                            bloom = session.pastHorizon,
                            modifier = Modifier.size(260.dp),
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                    Row(horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md)) {
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
                    Row(horizontalArrangement = Arrangement.spacedBy(JengaTheme.spacing.md)) {
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

@Composable
private fun TimerRing(
    fraction: Float,
    bloom: Boolean,
    modifier: Modifier = Modifier,
) {
    val track = JengaTheme.colors.border
    val accent = JengaTheme.colors.brand
    Canvas(modifier) {
        val strokeWidth = size.minDimension * 0.06f
        val diameter = size.minDimension - strokeWidth
        val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
        val arcSize = Size(diameter, diameter)
        val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Round)

        drawArc(
            color = track,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = stroke,
        )
        drawArc(
            color = accent,
            startAngle = -90f,
            sweepAngle = 360f * fraction.coerceIn(0f, 1f),
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = stroke,
        )
        if (bloom) {
            drawArc(
                color = accent.copy(alpha = 0.35f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth * 0.5f, cap = StrokeCap.Round),
            )
        }
    }
}

private fun Duration.asClock(): String {
    val totalSeconds = inWholeSeconds
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}
