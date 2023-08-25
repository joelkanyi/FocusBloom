package com.joelkanyi.focusbloom.android.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joelkanyi.focusbloom.android.domain.model.Task
import com.joelkanyi.focusbloom.android.ui.screens.destinations.AllTasksScreenDestination
import com.joelkanyi.focusbloom.android.ui.screens.destinations.FocusTimeScreenDestination
import com.joelkanyi.focusbloom.android.ui.screens.home.component.TaskCard
import com.joelkanyi.focusbloom.android.ui.screens.home.component.TaskProgress
import com.joelkanyi.focusbloom.android.ui.theme.FocusBloomTheme
import com.joelkanyi.focusbloom.samples.tasks
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true) // sets this as the start destination of the default nav graph
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
) {
    HomeScreenContent(
        onClickTask = {
            navigator.navigate(FocusTimeScreenDestination(taskId = it.id))
        },
        onClickSeeAllTasks = {
            navigator.navigate(AllTasksScreenDestination)
        },
    )
}

@Composable
private fun HomeScreenContent(
    onClickTask: (task: Task) -> Unit = {},
    onClickSeeAllTasks: () -> Unit = {},
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Text(
                        text = "Hello, Joel",
                        style = MaterialTheme.typography.displaySmall,
                    )
                }
                item {
                    Card {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            TaskProgress(
                                mainColor = MaterialTheme.colorScheme.secondary,
                                percentage = 75f,
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Text(
                                    text = "Wow!, Your daily tasks are almost done",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                    ),
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "12 of 16 tasks completed",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                    ),
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Today's Tasks (${tasks.size})",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                        )
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
                items(tasks.take(4)) {
                    TaskCard(
                        task = it,
                        onClick = onClickTask,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenContentPreview() {
    FocusBloomTheme {
        HomeScreenContent()
    }
}
