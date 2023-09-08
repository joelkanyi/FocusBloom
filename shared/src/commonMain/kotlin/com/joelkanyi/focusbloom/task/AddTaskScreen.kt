package com.joelkanyi.focusbloom.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import com.joelkanyi.focusbloom.core.presentation.component.BloomButton
import com.joelkanyi.focusbloom.core.presentation.component.BloomDateBoxField
import com.joelkanyi.focusbloom.core.presentation.component.BloomDropDown
import com.joelkanyi.focusbloom.core.presentation.component.BloomIncrementer
import com.joelkanyi.focusbloom.core.presentation.component.BloomInputTextField
import com.joelkanyi.focusbloom.core.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.core.utils.calculateFromFocusSessions
import com.joelkanyi.focusbloom.core.utils.selectedDateMillisToLocalDateTime
import com.joelkanyi.focusbloom.domain.model.TextFieldState
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddTaskScreen : Screen, KoinComponent {
    val screenModel: AddTaskScreenModel by inject()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val sessionTime = screenModel.sessionTime.collectAsState()
        val shortBreakTime = screenModel.shortBreakTime.collectAsState()
        val longBreakTime = screenModel.longBreakTime.collectAsState()
        val timeFormat = screenModel.timeFormat.collectAsState()
        var taskName by remember { mutableStateOf("") }
        var taskDescription by remember { mutableStateOf("") }
        var focusSessions = screenModel.focusSessions.collectAsState().value
        val taskTypes = listOf("Work", "Study", "Personal", "Other")
        var selectedOption by remember { mutableStateOf(taskTypes.last()) }
        val startTimeState = rememberTimePickerState(
            initialHour = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())
                .toLocalDateTime(TimeZone.currentSystemDefault()).hour,
            initialMinute = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())
                .toLocalDateTime(TimeZone.currentSystemDefault()).minute,
            is24Hour = false,
        )
        val endTimeState = rememberTimePickerState(
            initialHour = 5,
            initialMinute = 30,
            is24Hour = false,
        )
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds(),
        )
        var showStartTimeInputDialog by remember { mutableStateOf(false) }
        var showEndTimeInputDialog by remember { mutableStateOf(false) }
        var showTaskDatePickerDialog by remember { mutableStateOf(false) }

        if (showStartTimeInputDialog) {
            TimerInputDialog(
                title = "Start Time",
                state = startTimeState,
                onDismiss = {
                    showStartTimeInputDialog = false
                },
            )
        }

        if (showEndTimeInputDialog) {
            TimerInputDialog(
                title = "End Time",
                state = endTimeState,
                onDismiss = {
                    showEndTimeInputDialog = false
                },
            )
        }

        if (showTaskDatePickerDialog) {
            TaskDatePicker(
                datePickerState = datePickerState,
                dismiss = {
                    showTaskDatePickerDialog = false
                },
            )
        }

        AddTaskScreenContent(
            sessionTime = sessionTime.value,
            shortBreakTime = shortBreakTime.value,
            longBreakTime = longBreakTime.value,
            timeFormat = timeFormat.value,
            taskOptions = taskTypes,
            selectedOption = selectedOption,
            taskName = taskName,
            taskDescription = taskDescription,
            datePickerState = datePickerState,
            focusSessions = focusSessions,
            startTimePickerState = startTimeState,
            endTimePickerState = endTimeState,
            onTaskNameChange = {
                taskName = it
            },
            onIncrementFocusSessions = {
                screenModel.incrementFocusSessions()
            },
            onDecrementIncrementFocusSessions = {
                screenModel.decrementFocusSessions()
            },
            onClickAddTask = {
            },
            onSelectedOptionChange = {
                selectedOption = it
            },
            onTaskDescriptionChange = {
                taskDescription = it
            },
            onClickPickStartTime = {
                showStartTimeInputDialog = showStartTimeInputDialog.not()
            },
            onClickPickEndTime = {
                showEndTimeInputDialog = showEndTimeInputDialog.not()
            },
            onClickPickDate = {
                showTaskDatePickerDialog = showTaskDatePickerDialog.not()
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun AddTaskScreenContent(
    sessionTime: Int,
    shortBreakTime: Int,
    longBreakTime: Int,
    timeFormat: Int,
    taskOptions: List<String>,
    selectedOption: String,
    onSelectedOptionChange: (String) -> Unit,
    taskName: String,
    taskDescription: String,
    onTaskDescriptionChange: (String) -> Unit,
    focusSessions: Int,
    onTaskNameChange: (String) -> Unit,
    onIncrementFocusSessions: () -> Unit,
    onDecrementIncrementFocusSessions: () -> Unit,
    onClickAddTask: () -> Unit,
    onClickPickStartTime: () -> Unit,
    onClickPickEndTime: () -> Unit,
    onClickPickDate: () -> Unit,
    startTimePickerState: TimePickerState,
    endTimePickerState: TimePickerState,
    datePickerState: DatePickerState,
) {
    Scaffold(
        topBar = {
            BloomTopAppBar {
                Text(text = "Add Task")
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                BloomInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = "Task Name",
                            style = MaterialTheme.typography.titleSmall,

                        )
                    },
                    value = TextFieldState(text = taskName),
                    onValueChange = onTaskNameChange,
                    placeholder = {
                        Text(
                            text = "Enter Task Name",
                            style = MaterialTheme.typography.titleSmall,

                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Words,
                    ),
                )
            }
            item {
                BloomInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleSmall,

                        )
                    },
                    value = TextFieldState(text = taskDescription),
                    onValueChange = onTaskDescriptionChange,
                    placeholder = {
                        Text(text = "Enter Description")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences,
                    ),
                )
            }
            item {
                BloomDateBoxField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text(
                            text = "Date",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    },
                    currentTextState = TextFieldState(text = datePickerState.selectedDateMillis.selectedDateMillisToLocalDateTime().date.toString()),
                    onClick = onClickPickDate,
                )
            }

            item {
                BloomDropDown(
                    label = {
                        Text(text = "Task Type")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    options = taskOptions,
                    selectedOption = TextFieldState(selectedOption),
                    onOptionSelected = onSelectedOptionChange,
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(
                        modifier = Modifier,
                    ) {
                        Text(
                            text = "Start Time",
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onClickPickStartTime()
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = CenterVertically,
                        ) {
                            Text(text = "${startTimePickerState.hour}:${startTimePickerState.minute}")
                            IconButton(
                                onClick = onClickPickStartTime,
                            ) {
                                Icon(
                                    painter = painterResource("ic_time.xml"),
                                    contentDescription = "Start Time Picker",
                                )
                            }
                        }
                    }

                    Divider(
                        modifier = Modifier
                            .width(180.dp),
                        thickness = 1.dp,
                    )

                    Column(
                        modifier = Modifier,
                    ) {
                        Text(
                            text = "End Time",
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = CenterVertically,
                        ) {
                            Text(
                                text = "${
                                    calculateFromFocusSessions(
                                        focusSessions = focusSessions,
                                        sessionTime = sessionTime,
                                        shortBreakTime = shortBreakTime,
                                        longBreakTime = longBreakTime,
                                    )
                                }",
                            )
                            IconButton(
                                onClick = { },
                            ) {
                                Icon(
                                    painter = painterResource("ic_time.xml"),
                                    contentDescription = "End Time Picker",
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Focus Sessions",
                    style = MaterialTheme.typography.titleMedium.copy(
                        textAlign = TextAlign.Center,
                    ),
                )
            }

            item {
                BloomIncrementer(
                    modifier = Modifier.fillMaxWidth(),
                    onClickRemove = {
                        onDecrementIncrementFocusSessions()
                    },
                    onClickAdd = {
                        onIncrementFocusSessions()
                    },
                    currentValue = focusSessions,
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                BloomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    onClick = onClickAddTask,
                    content = {
                        Text(text = "Add Task")
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerInputDialog(
    title: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    state: TimePickerState,
) {
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = true),
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        text = {
            TimeInput(
                modifier = Modifier.fillMaxWidth(),
                state = state,
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                content = {
                    Text(text = "Cancel")
                },
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                content = {
                    Text(text = "OK")
                },
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDatePicker(
    datePickerState: DatePickerState,
    dismiss: () -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = { dismiss() },
        dismissButton = {
            TextButton(onClick = dismiss) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState
                        .selectedDateMillis
                    dismiss()
                },
            ) {
                Text(text = "OK")
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}
