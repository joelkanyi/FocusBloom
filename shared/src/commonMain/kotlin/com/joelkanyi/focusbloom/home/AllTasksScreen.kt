package com.joelkanyi.focusbloom.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.core.presentation.component.TaskCard
import com.joelkanyi.focusbloom.taskprogress.FocusTimeScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AllTasksScreen : Screen, KoinComponent {
    private val screenModel: HomeScreenModel by inject()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val allTasks = screenModel.tasks.collectAsState().value
        val hourFormat = screenModel.hourFormat.collectAsState().value
        AllTasksScreenContent(
            tasks = allTasks,
            timeFormat = hourFormat,
            onClickNavigateBack = {
                navigator.pop()
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
            onClickTask = {
                navigator.push(FocusTimeScreen(taskId = it.id))
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTasksScreenContent(
    tasks: List<Task>,
    timeFormat: Int,
    onClickNavigateBack: () -> Unit,
    onClickDelete: (task: Task) -> Unit,
    onClickCancel: (task: Task) -> Unit,
    onClickSave: (task: Task) -> Unit,
    showTaskOption: (task: Task) -> Boolean,
    onShowTaskOption: (task: Task) -> Unit,
    onClickTask: (task: Task) -> Unit,
) {
    Scaffold(
        topBar = {
            BloomTopAppBar(
                hasBackNavigation = true,
                navigationIcon = {
                    IconButton(onClick = onClickNavigateBack) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            ) {
                Text(text = "Today's Tasks (${tasks.size})")
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(tasks) {
                TaskCard(
                    task = it,
                    hourFormat = timeFormat,
                    onClick = onClickTask,
                    onClickDelete = onClickDelete,
                    onClickCancel = onClickCancel,
                    onClickSave = onClickSave,
                    showTaskOption = showTaskOption,
                    onShowTaskOption = onShowTaskOption,
                )
            }
        }
    }
}
