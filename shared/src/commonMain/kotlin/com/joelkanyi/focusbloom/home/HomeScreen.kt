package com.joelkanyi.focusbloom.home

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.presentation.component.TaskCard
import com.joelkanyi.focusbloom.core.presentation.component.TaskProgress
import com.joelkanyi.focusbloom.core.utils.taskCompleteMessage
import com.joelkanyi.focusbloom.core.utils.taskCompletionPercentage
import com.joelkanyi.focusbloom.taskprogress.FocusTimeScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeScreen : Screen, KoinComponent {
    private val screenModel: HomeScreenModel by inject()

    @Composable
    override fun Content() {
        val tasks = screenModel.tasks.collectAsState(emptyList()).value
        val hourFormat = screenModel.hourFormat.collectAsState().value
        val navigator = LocalNavigator.currentOrThrow
        HomeScreenContent(
            tasks = tasks,
            hourFormat = hourFormat,
            onClickTask = {
                navigator.push(FocusTimeScreen(taskId = it.id))
            },
            onClickCancel = {
                screenModel.openTaskOptions(it)
            },
            onClickSave = {
                screenModel.updateTask(it)
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
            onClickSeeAllTasks = {
                navigator.push(AllTasksScreen())
            },
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun HomeScreenContent(
    tasks: List<Task>,
    hourFormat: Int,
    onClickTask: (task: Task) -> Unit,
    onClickSeeAllTasks: () -> Unit,
    onClickCancel: (task: Task) -> Unit,
    onClickSave: (task: Task) -> Unit,
    onClickDelete: (task: Task) -> Unit,
    showTaskOption: (task: Task) -> Boolean,
    onShowTaskOption: (task: Task) -> Unit,
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
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
                                percentage = taskCompletionPercentage(tasks).toFloat(),
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Text(
                                    text = taskCompleteMessage(tasks),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                    ),
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${tasks.filter { it.completed }.size} of ${tasks.size} tasks completed",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                    ),
                                )
                            }
                        }
                    }
                }
                if (tasks.isNotEmpty()) {
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
                            if (tasks.size > 3) {
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
                }
                items(tasks.take(3)) {
                    TaskCard(
                        task = it,
                        onClick = onClickTask,
                        onClickDelete = onClickDelete,
                        onClickCancel = onClickCancel,
                        onClickSave = onClickSave,
                        showTaskOption = showTaskOption,
                        onShowTaskOption = onShowTaskOption,
                        hourFormat = hourFormat,
                    )
                }

                if (tasks.all { it.completed } || tasks.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = CenterHorizontally,
                        ) {
                            Spacer(modifier = Modifier.height(56.dp))
                            Image(
                                modifier = Modifier
                                    .size(200.dp)
                                    .align(CenterHorizontally),
                                painter = painterResource("empty_two.xml"),
                                contentDescription = null,
                            )
                            Text(
                                modifier = Modifier
                                    .align(CenterHorizontally),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 18.sp,
                                ),
                                text = if (tasks.isEmpty()) {
                                    "No tasks for today"
                                } else if (tasks.all { it.completed }) {
                                    "You've completed all your tasks for today"
                                } else {
                                    ""
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
