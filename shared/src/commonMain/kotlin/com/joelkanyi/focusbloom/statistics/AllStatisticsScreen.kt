package com.joelkanyi.focusbloom.statistics

import androidx.compose.foundation.layout.Arrangement
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AllStatisticsScreen : Screen, KoinComponent {
    private val screenModel: StatisticsScreenModel by inject()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val tasksHistory = screenModel.tasks.collectAsState().value
        AllStatisticsScreenContent(
            tasks = tasksHistory,
            onClickNavigateBack = {
                navigator.pop()
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllStatisticsScreenContent(
    tasks: List<Task>,
    onClickNavigateBack: () -> Unit,
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(tasks) {
                HistoryCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    task = it,
                )
            }
        }
    }
}
