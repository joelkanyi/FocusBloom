package com.joelkanyi.focusbloom.statistics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.core.presentation.component.durationInMinutes
import com.joelkanyi.focusbloom.core.utils.getLast12Weeks
import com.joelkanyi.focusbloom.core.utils.taskColor
import com.joelkanyi.focusbloom.core.utils.taskIcon
import com.joelkanyi.focusbloom.statistics.component.BarSamplePlot
import com.joelkanyi.focusbloom.statistics.component.TickPositionState
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xychart.TickPosition
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StatisticsScreen : Screen, KoinComponent {
    private val screenModel: StatisticsScreenModel by inject()

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val lastTwelveWeeks = getLast12Weeks().asReversed()
        val tasksHistory = screenModel.tasks.collectAsState().value
        val coroutineScope = rememberCoroutineScope()
        val pagerState = rememberPagerState(
            initialPage = lastTwelveWeeks.size - 1,
            initialPageOffsetFraction = 0f,
            pageCount = {
                lastTwelveWeeks.size
            },
        )

        val navigator = LocalNavigator.currentOrThrow
        StatisticsScreenContent(
            pagerState = pagerState,
            selectedWeek = lastTwelveWeeks[pagerState.currentPage].first,
            tasksHistory = tasksHistory,
            onClickSeeAllTasks = {
                navigator.push(AllStatisticsScreen())
            },
            onClickThisWeek = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(lastTwelveWeeks.size - 1)
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
        )
    }
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
    tasksHistory: List<Task>,
    onClickSeeAllTasks: () -> Unit,
    pagerState: PagerState,
    onClickNextWeek: () -> Unit,
    onClickPreviousWeek: () -> Unit,
    onClickThisWeek: () -> Unit,
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
                    val tickPositionState by remember {
                        mutableStateOf(
                            TickPositionState(
                                TickPosition.Outside,
                                TickPosition.Outside,
                            ),
                        )
                    }

                    ChartLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .sizeIn(maxHeight = 300.dp),
                    ) {
                        BarSamplePlot(false, tickPositionState, "Your Weekly Statistics")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
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
                                ),
                            )
                        }
                    }
                }
            }

            items(tasksHistory.take(3)) { history ->
                HistoryCard(
                    task = history,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                )
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
            onClick = onClickNextWeek,
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardDoubleArrowRight,
                contentDescription = "Next Week",
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HistoryCard(
    task: Task,
    modifier: Modifier = Modifier,
) {
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
                        text = task.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        modifier = Modifier.size(18.dp),
                    )
                }
                if (task.description != null) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "${task.durationInMinutes()} minutes",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )
                    Text(
                        "${
                            task.date.date
                        }",
                        /*text = "${task.start.format(TaskTimeFormatter)} - ${
                            task.end.format(
                                TaskTimeFormatter,
                            )
                        }",*/
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )
                }
            }
        }
    }
}
