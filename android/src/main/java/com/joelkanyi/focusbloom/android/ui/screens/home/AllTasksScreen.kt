package com.joelkanyi.focusbloom.android.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joelkanyi.focusbloom.android.component.BloomTopAppBar
import com.joelkanyi.focusbloom.presentation.component.TaskCard
import com.joelkanyi.focusbloom.android.ui.theme.FocusBloomTheme
import com.joelkanyi.samples.sampleTasks
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AllTasksScreen(
    navigator: DestinationsNavigator,
) {
    AllTasksScreenContent(
        onClickNavigateBack = {
            navigator.popBackStack()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTasksScreenContent(
    onClickNavigateBack: () -> Unit = {},
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
                Text(text = "Today's Tasks (${com.joelkanyi.samples.sampleTasks.size})")
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(com.joelkanyi.samples.sampleTasks) {
                    com.joelkanyi.focusbloom.presentation.component.TaskCard(
                        task = it,
                        onClick = { },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AllTasksScreenContentPreview() {
    FocusBloomTheme {
        AllTasksScreenContent()
    }
}
