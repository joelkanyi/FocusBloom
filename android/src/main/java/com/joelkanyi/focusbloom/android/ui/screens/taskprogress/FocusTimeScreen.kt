package com.joelkanyi.focusbloom.android.ui.screens.taskprogress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joelkanyi.focusbloom.android.component.BloomTimerControls
import com.joelkanyi.focusbloom.android.component.BloomTopAppBar
import com.joelkanyi.focusbloom.android.ui.screens.home.component.TaskProgress
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun FocusTimeScreen(
    taskId: Int,
    navigator: DestinationsNavigator,
) {
    FocusTimeScreenContent(
        onClickNavigateBack = {
            navigator.popBackStack()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusTimeScreenContent(onClickNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            BloomTopAppBar(
                hasBackNavigation = true,
                navigationIcon = {
                    IconButton(onClick = onClickNavigateBack) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Add Task Back Button",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(.85f),
                                text = "Add iOS Cocoapods Support to the Multiplatform Project Shared Gradle",
                                style = MaterialTheme.typography.titleSmall,
                            )
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.End,
                            ) {
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 18.sp,
                                            ),
                                        ) {
                                            append("4")
                                        }
                                        append("/5")
                                    },
                                )
                                Text(text = "25 min")
                            }
                        }

                        Text(
                            text = "120 minutes",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TaskProgress(
                        percentage = 36f,
                        radius = 50.dp,
                        content = "23:54",
                        mainColor = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Focus Time",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                )
            }

            item {
                Spacer(modifier = Modifier.height(56.dp))
                BloomTimerControls(
                    modifier = Modifier.fillMaxWidth(),
                    onClickReset = { /*TODO*/ },
                    onClickNext = { /*TODO*/ },
                    onClickAction = { /*TODO*/ },
                )
            }
        }
    }
}
