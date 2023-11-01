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
package com.joelkanyi.focusbloom.feature.statistics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.currentOrThrow
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.core.utils.LocalAppNavigator
import com.joelkanyi.focusbloom.core.utils.calculateEndTime
import com.joelkanyi.focusbloom.core.utils.completedTasks
import com.joelkanyi.focusbloom.core.utils.durationInMinutes
import com.joelkanyi.focusbloom.core.utils.getLast52Weeks
import com.joelkanyi.focusbloom.core.utils.prettyFormat
import com.joelkanyi.focusbloom.core.utils.prettyTimeDifference
import com.joelkanyi.focusbloom.core.utils.taskColor
import com.joelkanyi.focusbloom.core.utils.taskIcon
import com.joelkanyi.focusbloom.feature.statistics.component.BarChart
import com.joelkanyi.focusbloom.feature.statistics.component.TickPositionState
import com.joelkanyi.focusbloom.platform.StatusBarColors
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xychart.TickPosition
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatisticsScreen(
    screenModel: StatisticsScreenModel = koinInject(),
) {
    StatusBarColors(
        statusBarColor = MaterialTheme.colorScheme.background,
        navBarColor = MaterialTheme.colorScheme.background,
    )
    val navigator = LocalAppNavigator.currentOrThrow
    val tasksHistory = screenModel.tasks.collectAsState().value
    val lastFiftyTwoWeeks = getLast52Weeks().asReversed()
    val hourFormat = screenModel.hourFormat.collectAsState().value ?: 24
    val sessionTime = screenModel.sessionTime.collectAsState().value ?: 25
    val shortBreakTime = screenModel.shortBreakTime.collectAsState().value ?: 5
    val longBreakTime = screenModel.longBreakTime.collectAsState().value ?: 15
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = lastFiftyTwoWeeks.size - 1,
        initialPageOffsetFraction = 0f,
        pageCount = {
            lastFiftyTwoWeeks.size
        },
    )
    val selectedWeek = lastFiftyTwoWeeks[pagerState.currentPage].first
    val selectedWeekTasks = tasksHistory.completedTasks(
        lastFiftyTwoWeeks[pagerState.currentPage].second,
    ).map { it.toFloat() }
    val tickPositionState by remember {
        mutableStateOf(
            TickPositionState(
                TickPosition.Outside,
                TickPosition.Outside,
            ),
        )
    }

    StatisticsScreenContent(
        hourFormat = hourFormat,
        sessionTime = sessionTime,
        tickPositionState = tickPositionState,
        shortBreakTime = shortBreakTime,
        longBreakTime = longBreakTime,
        pagerState = pagerState,
        selectedWeek = selectedWeek,
        selectedWeekTasks = selectedWeekTasks,
        tasksHistory = tasksHistory,
        onClickSeeAllTasks = {
            navigator.push(AllStatisticsScreen())
        },
        onClickThisWeek = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(lastFiftyTwoWeeks.size - 1)
            }
        },
        onClickNextWeek = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        },
        onClickPreviousWeek = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        },
        onClickDelete = {
            screenModel.deleteTask(it)
        },
        showTaskOption = {
            screenModel.openedTasks.contains(it)
        },
        onShowTaskOption = {
            screenModel.openTaskOptions(it)
        },
        onClickCancel = {
            screenModel.openTaskOptions(it)
        },
    )
}

@OptIn(
    ExperimentalKoalaPlotApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalResourceApi::class,
)
@Composable
fun StatisticsScreenContent(
    selectedWeek: String,
    hourFormat: Int,
    sessionTime: Int,
    shortBreakTime: Int,
    longBreakTime: Int,
    selectedWeekTasks: List<Float>,
    tickPositionState: TickPositionState,
    tasksHistory: List<Task>,
    onClickSeeAllTasks: () -> Unit,
    pagerState: PagerState,
    onClickNextWeek: () -> Unit,
    onClickPreviousWeek: () -> Unit,
    onClickThisWeek: () -> Unit,
    onClickDelete: (task: Task) -> Unit,
    onClickCancel: (task: Task) -> Unit,
    showTaskOption: (task: Task) -> Boolean,
    onShowTaskOption: (task: Task) -> Unit,
) {
    Scaffold(
        topBar = {
            BloomTopAppBar(
                hasBackNavigation = false,
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(.7f),
                        text = "Your Statistics",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                },
                actions = {
                    AnimatedVisibility(selectedWeek != "This Week") {
                        TextButton(
                            onClick = onClickThisWeek,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Icon(
                                    modifier = Modifier.size(18.dp),
                                    painter = painterResource("redo.xml"),
                                    contentDescription = "This Week",
                                )
                                Text(
                                    text = "This Week",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary,
                                        textDecoration = TextDecoration.Underline,
                                    ),
                                )
                            }
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            item {
                WeeksController(
                    onClickPreviousWeek = onClickPreviousWeek,
                    selectedWeek = selectedWeek,
                    onClickNextWeek = onClickNextWeek,
                )
            }
            item {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    ChartLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .sizeIn(maxHeight = 300.dp),
                    ) {
                        BarChart(
                            tickPositionState = tickPositionState,
                            entries = selectedWeekTasks,
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Your History",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                    if (tasksHistory.size > 3) {
                        TextButton(onClick = onClickSeeAllTasks) {
                            Text(
                                text = "See All",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 16.sp,
                                ),
                            )
                        }
                    }
                }
            }
            val grouped = tasksHistory.take(3).groupBy { it.date.date }
            grouped.forEach { (date, tasks) ->
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 6.dp),
                        text = date.prettyFormat(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.End,
                        ),
                    )
                }
                items(
                    items = tasks,
                    key = { it.id },
                ) {
                    HistoryCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        task = it,
                        hourFormat = hourFormat,
                        sessionTime = sessionTime,
                        shortBreakTime = shortBreakTime,
                        longBreakTime = longBreakTime,
                        onClickDelete = onClickDelete,
                        onClickCancel = onClickCancel,
                        showTaskOption = showTaskOption,
                        onShowTaskOption = onShowTaskOption,
                    )
                }
            }
        }
    }
}

@Composable
private fun WeeksController(
    onClickPreviousWeek: () -> Unit,
    selectedWeek: String,
    onClickNextWeek: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier.size(24.dp),
            onClick = onClickPreviousWeek,
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardDoubleArrowLeft,
                contentDescription = "Previous Week",
            )
        }
        Text(
            text = selectedWeek,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
            ),
        )
        IconButton(
            modifier = Modifier.size(24.dp),
            onClick = {
                if (selectedWeek != "This Week") {
                    onClickNextWeek()
                }
            },
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardDoubleArrowRight,
                contentDescription = "Next Week",
                tint = if (selectedWeek != "This Week") {
                    MaterialTheme.colorScheme.onBackground
                } else {
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                },
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HistoryCard(
    task: Task,
    modifier: Modifier = Modifier,
    hourFormat: Int,
    sessionTime: Int,
    shortBreakTime: Int,
    longBreakTime: Int,
    onClickCancel: (task: Task) -> Unit,
    onClickDelete: (task: Task) -> Unit,
    showTaskOption: (task: Task) -> Boolean,
    onShowTaskOption: (task: Task) -> Unit,
) {
    Column {
        Card(
            modifier = modifier,
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(
                            color = Color(task.type.taskColor()),
                            shape = MaterialTheme.shapes.medium,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(12.dp)
                            .size(24.dp),
                        painter = painterResource(task.type.taskIcon()),
                        contentDescription = "Task Icon",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(.8f),
                            text = task.name,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Icon(
                            modifier = Modifier
                                .size(18.dp)
                                .clickable {
                                    onShowTaskOption(task)
                                },
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                        )
                    }
                    if (task.description != null) {
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "${
                            task.durationInMinutes(
                                focusSessions = task.focusSessions,
                                sessionTime = sessionTime,
                                shortBreakTime = shortBreakTime,
                                longBreakTime = longBreakTime,
                            )
                            } minutes",
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                            ),
                        )
                        Text(
                            prettyTimeDifference(
                                start = task.start,
                                end = task.start.calculateEndTime(
                                    focusSessions = task.focusSessions,
                                    sessionTime = sessionTime,
                                    shortBreakTime = shortBreakTime,
                                    longBreakTime = longBreakTime,
                                ),
                                timeFormat = hourFormat,
                            ),
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                            ),
                        )
                    }
                }
            }
            AnimatedVisibility(visible = showTaskOption(task)) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextButton(onClick = {
                            onClickCancel(task)
                        }) {
                            Text(
                                text = "Cancel",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                ),
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        TextButton(onClick = {
                            onClickDelete(task)
                        }) {
                            Text(
                                text = "Delete",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}
