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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import androidx.navigation.NavController
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.core.utils.koinViewModel
import com.joelkanyi.focusbloom.core.utils.prettyFormat
import com.joelkanyi.focusbloom.platform.StatusBarColors
import kotlinx.datetime.LocalDate

@Composable
fun AllStatisticsScreen(
    navController: NavController,
    viewModel: StatisticsViewModel = koinViewModel()
) {
    StatusBarColors(
        statusBarColor = MaterialTheme.colorScheme.background,
        navBarColor = MaterialTheme.colorScheme.background,
    )
    val hourFormat = viewModel.hourFormat.collectAsState().value ?: 24
    val sessionTime = viewModel.sessionTime.collectAsState().value ?: 25
    val shortBreakTime = viewModel.shortBreakTime.collectAsState().value ?: 5
    val longBreakTime = viewModel.longBreakTime.collectAsState().value ?: 15
    val tasksHistory = viewModel.tasks.collectAsState().value.groupBy { it.date.date }

    AllStatisticsScreenContent(
        timeFormat = hourFormat,
        sessionTime = sessionTime,
        shortBreakTime = shortBreakTime,
        longBreakTime = longBreakTime,
        tasks = tasksHistory,
        onClickNavigateBack = {
            navController.popBackStack()
        },
        onClickDelete = {
            viewModel.deleteTask(it)
        },
        showTaskOption = {
            viewModel.openedTasks.contains(it)
        },
        onShowTaskOption = {
            viewModel.openTaskOptions(it)
        },
        onClickCancel = {
            viewModel.openTaskOptions(it)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AllStatisticsScreenContent(
    timeFormat: Int,
    sessionTime: Int,
    shortBreakTime: Int,
    longBreakTime: Int,
    tasks: Map<LocalDate, List<Task>>,
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
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
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
            tasks.forEach { (date, tasks) ->
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
                items(
                    items = tasks,
                    key = { it.id },
                ) {
                    HistoryCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        task = it,
                        hourFormat = timeFormat,
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
