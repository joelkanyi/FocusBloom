package com.joelkanyi.focusbloom.android.ui.screens.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.joelkanyi.focusbloom.android.R
import com.joelkanyi.focusbloom.android.component.BloomButton
import com.joelkanyi.focusbloom.android.component.BloomIncrementer
import com.joelkanyi.focusbloom.android.component.BloomInputTextField
import com.joelkanyi.focusbloom.android.component.BloomTopAppBar
import com.joelkanyi.focusbloom.android.domain.model.TextFieldState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun AddTaskScreen(
    navigator: DestinationsNavigator,
) {
    var taskName by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var focusSessions by remember { mutableIntStateOf(0) }

    AddTaskScreenContent(
        taskName = taskName,
        date = date,
        focusSessions = focusSessions,
        onClickNavigateBack = {
            navigator.popBackStack()
        },
        onDateChange = {
            date = it
        },
        onTaskNameChange = {
            taskName = it
        },
        onIncrementFocusSessions = {
            focusSessions = it
        },
        onClickAddTask = {
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTaskScreenContent(
    taskName: String,
    date: String,
    focusSessions: Int,
    onClickNavigateBack: () -> Unit,
    onTaskNameChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onIncrementFocusSessions: (Int) -> Unit,
    onClickAddTask: () -> Unit,
) {
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
            ) {
                Text(text = stringResource(R.string.add_task))
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                BloomInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.task))
                    },
                    value = TextFieldState(text = taskName),
                    onValueChange = onTaskNameChange,
                    placeholder = {
                        Text(text = stringResource(R.string.enter_task_name))
                    },
                )
            }
            item {
                BloomInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.date))
                    },
                    value = TextFieldState(text = date),
                    onValueChange = onDateChange,
                    placeholder = {
                        Text(text = stringResource(R.string.enter_date))
                    },
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Outlined.DateRange,
                                contentDescription = "Date Picker",
                            )
                        }
                    },
                )
            }

            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.focus_sessions),
                    style = MaterialTheme.typography.titleMedium.copy(
                        textAlign = TextAlign.Center,
                    ),
                )
            }

            item {
                BloomIncrementer(
                    modifier = Modifier.fillMaxWidth(),
                    onClickRemove = {
                        if (focusSessions > 0) {
                            onIncrementFocusSessions(focusSessions - 1)
                        }
                    },
                    onClickAdd = {
                        onIncrementFocusSessions(focusSessions + 1)
                    },
                    currentValue = focusSessions,
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                BloomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    onClick = onClickAddTask,
                    content = {
                        Text(text = stringResource(R.string.add_task))
                    },
                )
            }
        }
    }
}
