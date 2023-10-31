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

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskProgress(
    content: String? = null,
    percentage: Float,
    radius: Dp = 20.dp,
    mainColor: Color,
    counterColor: Color,
    strokeWidth: Dp = 8.dp,
    animationDuration: Int = 800,
    animDelay: Int = 0,
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val currentPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animDelay,
        ),
        label = "",
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier
                .size(radius * 5f),
        ) {
            drawArc(
                color = Color.LightGray,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
            )

            drawArc(
                color = mainColor,
                startAngle = -360f,
                sweepAngle = (360 * (currentPercentage.value * 0.01)).toFloat(),
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape),
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (content?.isEmpty()?.not() == true) {
                    Text(
                        text = content,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                } else {
                    Text(
                        text = "${(currentPercentage.value).toInt()}%",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = counterColor,
                    )
                }
            }
        }
    }
}
