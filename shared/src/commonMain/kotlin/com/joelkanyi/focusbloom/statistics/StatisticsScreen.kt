package com.joelkanyi.focusbloom.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.joelkanyi.focusbloom.core.presentation.component.BloomDropDown
import com.joelkanyi.focusbloom.core.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.core.presentation.component.durationInMinutes
import com.joelkanyi.focusbloom.core.samples.sampleTasks
import com.joelkanyi.focusbloom.domain.model.TextFieldState
import com.joelkanyi.focusbloom.domain.model.Task
import com.joelkanyi.focusbloom.statistics.component.StatsChart
import com.joelkanyi.focusbloom.statistics.component.statsData

class StatisticsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        var selectedOption by remember { mutableStateOf("This Week") }
        Scaffold(
            topBar = {
                BloomTopAppBar(
                    hasBackNavigation = false,
                ) {
                    Text(text = "Statistics")
                }
            },
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(.7f),
                            text = "Your Statistics Graph",
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontSize = 18.sp,
                            ),
                        )
                        BloomDropDown(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            options = listOf(
                                "This Week",
                                "This Month",
                                "This Year",
                            ),
                            selectedOption = TextFieldState(selectedOption),
                            onOptionSelected = {
                                selectedOption = it
                            },
                            shape = RoundedCornerShape(50),
                        )
                    }
                }

                item {
                    StatsChart(
                        modifier = Modifier
                            .fillMaxWidth(),
                        data = statsData,
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
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
                        TextButton(onClick = { }) {
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

                items(sampleTasks) { history ->
                    HistoryCard(
                        task = history,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryCard(
    task: Task,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "task.date.format(DayFormatter),",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary,
                    ),
                )
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options",
                    modifier = Modifier.size(18.dp),
                )
            }
            Text(
                text = task.name,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (task.description != null) {
                Text(
                    text = "task.description,",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
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
                    "",
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
