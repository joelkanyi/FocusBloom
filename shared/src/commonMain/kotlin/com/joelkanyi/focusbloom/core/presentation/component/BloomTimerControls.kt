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
package com.joelkanyi.focusbloom.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joelkanyi.focusbloom.feature.taskprogress.TimerState

@Composable
fun BloomTimerControls(
    modifier: Modifier = Modifier,
    state: TimerState,
    onClickReset: () -> Unit,
    onClickNext: () -> Unit,
    onClickAction: (state: TimerState) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        IconButton(onClick = onClickReset) {
            Icon(
                modifier = Modifier.size(120.dp),
                imageVector = Icons.Filled.Replay,
                contentDescription = "Reset Timer",
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }

        BloomCircleButton(
            modifier = Modifier.size(84.dp),
            icon = {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = when (state) {
                        TimerState.Paused -> {
                            Icons.Filled.PlayArrow
                        }

                        TimerState.Ticking -> {
                            Icons.Filled.Pause
                        }

                        TimerState.Finished -> {
                            Icons.Filled.Replay
                        }

                        else -> {
                            Icons.Filled.PlayArrow
                        }
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            },
            onClick = {
                onClickAction(state)
            },
            color = MaterialTheme.colorScheme.primary,
        )

        IconButton(onClick = onClickNext) {
            Icon(
                modifier = Modifier.size(120.dp),
                imageVector = Icons.Filled.SkipNext,
                contentDescription = "Next Timer",
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}
