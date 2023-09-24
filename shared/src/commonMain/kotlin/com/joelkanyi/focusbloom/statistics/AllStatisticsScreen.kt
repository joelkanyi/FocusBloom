package com.joelkanyi.focusbloom.statistics

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.core.utils.prettyFormat
import com.joelkanyi.focusbloom.platform.StatusBarColors
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AllStatisticsScreen : Screen, KoinComponent {
    private val screenModel: StatisticsScreenModel by inject()

    @Composable
    override fun Content() {
        StatusBarColors(
            statusBarColor = MaterialTheme.colorScheme.background,
            navBarColor = MaterialTheme.colorScheme.background,
        )
        val navigator = LocalNavigator.currentOrThrow
        val hourFormat = screenModel.hourFormat.collectAsState().value
        val tasksHistory = screenModel.tasks.collectAsState().value

        AllStatisticsScreenContent(
            timeFormat = hourFormat,
            tasks = tasksHistory,
            onClickNavigateBack = {
                navigator.pop()
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
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AllStatisticsScreenContent(
    timeFormat: Int,
    tasks: List<Task>,
    onClickNavigateBack: () -> Unit,
    onClickDelete: (task: Task) -> Unit,
    onClickCancel: (task: Task) -> Unit,
    showTaskOption: (task: Task) -> Boolean,
    onShowTaskOption: (task: Task) -> Unit,
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
                Text(text = "Tasks History")
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            val groupedTasksHistory = tasks.groupBy { it.date.date }

            groupedTasksHistory.forEach { (date, tasks) ->
                stickyHeader {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(bottom = 4.dp),
                        text = date.prettyFormat(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        ),
                    )
                }
                items(tasks) {
                    HistoryCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        task = it,
                        hourFormat = timeFormat,
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
