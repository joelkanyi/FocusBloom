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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.currentOrThrow
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.presentation.component.TaskCard
import com.joelkanyi.focusbloom.core.presentation.component.TaskProgress
import com.joelkanyi.focusbloom.core.utils.LocalAppNavigator
import com.joelkanyi.focusbloom.core.utils.pickFirstName
import com.joelkanyi.focusbloom.core.utils.taskCompleteMessage
import com.joelkanyi.focusbloom.core.utils.taskCompletionPercentage
import com.joelkanyi.focusbloom.platform.StatusBarColors
import com.joelkanyi.focusbloom.taskprogress.FocusTimeScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.rememberKoinInject

@Composable
fun HomeScreen() {
    val screenModel: HomeScreenModel = rememberKoinInject()

    StatusBarColors(
        statusBarColor = MaterialTheme.colorScheme.background,
        navBarColor = MaterialTheme.colorScheme.background,
    )
    val tasks = screenModel.tasks.collectAsState(emptyList()).value
    val username = screenModel.username.collectAsState().value
    val hourFormat = screenModel.hourFormat.collectAsState().value
    val navigator = LocalAppNavigator.currentOrThrow
    HomeScreenContent(
        tasks = tasks.sortedByDescending { it.completed.not() },
        hourFormat = hourFormat,
        username = username,
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

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun HomeScreenContent(
    tasks: List<Task>,
    hourFormat: Int,
    username: String,
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
                        text = "Hello, ${username.pickFirstName()}!",
                        style = MaterialTheme.typography.displaySmall,
                    )
                }
                item {
                    if (tasks.isNotEmpty() && tasks.all { it.completed }.not()) {
                        TodayTaskProgressCard(tasks = tasks)
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
                if (tasks.all { it.completed }.not()) {
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
                }

                if (tasks.all { it.completed } || tasks.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = CenterHorizontally,
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Image(
                                modifier = Modifier
                                    .size(300.dp)
                                    .align(CenterHorizontally),
                                painter = painterResource(
                                    if (tasks.isEmpty()) "il_empty.xml" else "il_completed.xml",
                                ),
                                contentDescription = null,
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(CenterHorizontally),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                                text = if (tasks.isEmpty()) {
                                    "Start your day productively! Add your first task."
                                } else if (tasks.all { it.completed }) {
                                    "Great job! You've finished all your tasks for today."
                                } else {
                                    ""
                                },
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 24.dp)
                                    .fillMaxWidth(),
                                text = if (tasks.isEmpty()) {
                                    "To add a task, simply tap the '+' button at the bottom of the screen. Fill in the task details and tap 'Save'."
                                } else if (tasks.all { it.completed }) {
                                    "Now, take some time to have fun, recharge, maybe do some exercise, and consider opening your calendar to plan for tomorrow's tasks. Keep up the fantastic work!"
                                } else {
                                    ""
                                },
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 14.sp,
                                ),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TodayTaskProgressCard(tasks: List<Task>) {
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
