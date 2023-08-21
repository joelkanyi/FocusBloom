package com.joelkanyi.focusbloom.android.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joelkanyi.focusbloom.android.ui.screens.destinations.FocusTimeScreenDestination
import com.joelkanyi.focusbloom.android.ui.screens.home.component.TaskProgress
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
    )
}

@Composable
private fun HomeScreenContent(
    onClickTask: (task: Task) -> Unit = {},
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
                        Text(
                            text = "See All",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                            ),
                        )
                    }
                }
                items(tasks) {
                    TaskCard(
                        task = it,
                        onClick = onClickTask,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task,
    onClick: (task: Task) -> Unit,
) {
    var showTaskOption by remember {
        mutableStateOf(false)
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
                Text(
                    modifier = Modifier
                        .fillMaxWidth(.85f),
                    text = task.name,
                    style = MaterialTheme.typography.titleSmall,
                )
                Icon(
                    modifier = Modifier
                        .clickable {
                            showTaskOption = !showTaskOption
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
                                append("${task.current}")
                            }
                            append("/${task.totalCycles}")
                        },
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${task.duration} minutes",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
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

            AnimatedVisibility(visible = showTaskOption) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Delete",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                        )
                        Button(
                            shape = MaterialTheme.shapes.medium,
                            onClick = { /*TODO*/ },
                        ) {
                            Text(
                                text = "Save",
                            )
                        }
                    }
                }
            }
        }
    }
}

data class Task(
    val id: Int = 0,
    val name: String,
    val duration: Int,
    val current: Int,
    val totalCycles: Int,
)

val tasks = listOf(
    Task(
        name = "Long Task Name Will Get In This Part Of The Card And Will Be Truncated If It Is Too Long",
        duration = 120,
        current = 4,
        totalCycles = 8,
    ),
    Task(
        name = "Task 2 Will Get In This Part Of The Card And Will Be Truncated If It Is Too Long",
        duration = 120,
        current = 4,
        totalCycles = 8,
    ),
    Task(
        name = "Task 3 Will Get In This Part Of The Card And Will Be Truncated If It Is Too Long",
        duration = 120,
        current = 4,
        totalCycles = 8,
    ),
    Task(
        name = "Task 4 Will Get In This Part Of The Card And Will Be Truncated If It Is Too Long",
        duration = 120,
        current = 4,
        totalCycles = 8,
    ),
    Task(
        name = "Task 5 Will Get In This Part Of The Card And Will Be Truncated If It Is Too Long",
        duration = 120,
        current = 4,
        totalCycles = 8,
    ),
    Task(
        name = "Task 6 Will Get In This Part Of The Card And Will Be Truncated If It Is Too Long",
        duration = 120,
        current = 4,
        totalCycles = 8,
    ),
    Task(
        name = "Task 7 Will Get In This Part Of The Card And Will Be Truncated If It Is Too Long",
        duration = 120,
        current = 4,
        totalCycles = 8,
    ),
    Task(
        name = "Task 8 Will Get In This Part Of The Card And Will Be Truncated If It Is Too Long",
        duration = 120,
        current = 4,
        totalCycles = 8,
    ),
)
