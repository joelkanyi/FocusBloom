package com.joelkanyi.focusbloom.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BloomTimerControls(
    modifier: Modifier = Modifier,
    onClickReset: () -> Unit,
    onClickNext: () -> Unit,
    onClickAction: () -> Unit,
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
            )
        }

        BloomCircleButton(
            modifier = Modifier.size(84.dp),
            icon = {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Filled.Pause,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            },
            onClick = onClickAction,
            color = MaterialTheme.colorScheme.primary,
        )

        IconButton(onClick = onClickNext) {
            Icon(
                modifier = Modifier.size(120.dp),
                imageVector = Icons.Filled.SkipNext,
                contentDescription = "Next Timer",
            )
        }
    }
}
