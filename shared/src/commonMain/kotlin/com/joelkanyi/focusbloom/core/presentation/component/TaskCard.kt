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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.utils.calculateEndTime
import com.joelkanyi.focusbloom.core.utils.durationInMinutes
import com.joelkanyi.focusbloom.core.utils.prettyFormat
import com.joelkanyi.focusbloom.core.utils.prettyTimeDifference
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun TaskCard(
    task: Task,
    focusSessions: Int,
    sessionTime: Int,
    shortBreakTime: Int,
    longBreakTime: Int,
    hourFormat: Int,
    onClick: (task: Task) -> Unit,
    onShowTaskOption: (task: Task) -> Unit,
    type: String,
) {
    val end by remember {
        mutableStateOf(
            task.start.calculateEndTime(
                focusSessions = focusSessions,
                sessionTime = sessionTime,
                shortBreakTime = shortBreakTime,
                longBreakTime = longBreakTime,
            ),
        )
    }
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            onClick(task)
        },
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.85f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = task.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (task.description != null) {
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Icon(
                    modifier = Modifier
                        .clickable {
                            onShowTaskOption(task)
                        },
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Task Options",
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                ),
                            ) {
                                append("${task.currentCycle}")
                            }
                            append("/${task.focusSessions}")
                        },
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${
                        task.durationInMinutes(
                            focusSessions = focusSessions,
                            sessionTime = sessionTime,
                            shortBreakTime = shortBreakTime,
                            longBreakTime = longBreakTime,
                        )
                        } minutes",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = prettyTimeDifference(
                            start = task.start,
                            end = end,
                            timeFormat = hourFormat,
                        ),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    if (type == "overdue") {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.date.date.prettyFormat(),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                        )
                    }
                }
                if (task.completed) {
                    Image(
                        modifier = Modifier
                            .size(48.dp),
                        painter = painterResource("ic_complete.xml"),
                        contentDescription = "Task Options",
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Task Options",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
        }
    }
}
