package com.joelkanyi.focusbloom.task

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.model.TextFieldState
import com.joelkanyi.focusbloom.core.presentation.component.BloomButton
import com.joelkanyi.focusbloom.core.presentation.component.BloomDateBoxField
import com.joelkanyi.focusbloom.core.presentation.component.BloomDropDown
import com.joelkanyi.focusbloom.core.presentation.component.BloomIncrementer
import com.joelkanyi.focusbloom.core.presentation.component.BloomInputTextField
import com.joelkanyi.focusbloom.core.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.core.utils.UiEvents
import com.joelkanyi.focusbloom.core.utils.calculateFromFocusSessions
import com.joelkanyi.focusbloom.core.utils.selectedDateMillisToLocalDateTime
import com.joelkanyi.focusbloom.core.utils.toLocalDateTime
import com.joelkanyi.focusbloom.home.HomeScreen
import kotlinx.coroutines.flow.collectLatest
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

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val snackbarHostState = remember { SnackbarHostState() }
        val keyboardController = LocalSoftwareKeyboardController.current
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(key1 = true) {
            screenModel.eventsFlow.collectLatest { event ->
                when (event) {
                    is UiEvents.ShowSnackbar -> {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                        )
                    }

                    UiEvents.NavigateBack -> {
                        navigator.popUntil {
                            it is HomeScreen
                        }
                    }

                    else -> {}
                }
            }
        }
        val sessionTime = screenModel.sessionTime.collectAsState().value
        val shortBreakTime = screenModel.shortBreakTime.collectAsState().value
        val longBreakTime = screenModel.longBreakTime.collectAsState().value
        val timeFormat = screenModel.timeFormat.collectAsState().value
        val focusSessions = screenModel.focusSessions.collectAsState().value
        val taskTypes = screenModel.taskTypes
        val showStartTimeInputDialog = screenModel.showStartTimeInputDialog.collectAsState().value
        val showTaskDatePickerDialog = screenModel.showTaskDatePickerDialog.collectAsState().value
        val selectedOption = screenModel.selectedOption.collectAsState().value
        val taskName = screenModel.taskName.collectAsState().value
        val taskDescription = screenModel.taskDescription.collectAsState().value

        val startTimeState = rememberTimePickerState(
            initialHour = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())
                .toLocalDateTime(TimeZone.currentSystemDefault()).hour,
            initialMinute = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())
                .toLocalDateTime(TimeZone.currentSystemDefault()).minute,
            is24Hour = false,
        )
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds(),
        )

        if (showStartTimeInputDialog) {
            TimerInputDialog(
                title = "Start Time",
                state = startTimeState,
                onDismiss = {
                    screenModel.setShowStartTimeInputDialog(false)
                },
            )
        }

        if (showTaskDatePickerDialog) {
            TaskDatePicker(
                datePickerState = datePickerState,
                dismiss = {
                    screenModel.setShowTaskDatePickerDialog(false)
                },
            )
        }

        AddTaskScreenContent(
            snackbarHostState = snackbarHostState,
            sessionTime = sessionTime,
            shortBreakTime = shortBreakTime,
            longBreakTime = longBreakTime,
            timeFormat = timeFormat,
            taskOptions = taskTypes,
            selectedOption = selectedOption,
            taskName = taskName,
            taskDescription = taskDescription,
            datePickerState = datePickerState,
            focusSessions = focusSessions,
            startTimePickerState = startTimeState,
            onTaskNameChange = {
                screenModel.setTaskName(it)
            },
            onIncrementFocusSessions = {
                screenModel.incrementFocusSessions()
            },
            onDecrementIncrementFocusSessions = {
                screenModel.decrementFocusSessions()
            },
            onSelectedOptionChange = {
                screenModel.setSelectedOption(it)
            },
            onTaskDescriptionChange = {
                screenModel.setTaskDescription(it)
            },
            onClickPickStartTime = {
                screenModel.setShowStartTimeInputDialog(true)
            },
            onClickPickDate = {
                screenModel.setShowTaskDatePickerDialog(true)
            },
            onClickAddTask = {
                keyboardController?.hide()
                screenModel.addTask(
                    task = Task(
                        name = taskName,
                        description = taskDescription,
                        start = toLocalDateTime(
                            date = datePickerState.selectedDateMillis.selectedDateMillisToLocalDateTime().date,
                            hour = startTimeState.hour,
                            minute = startTimeState.minute,
                        ),
                        end = toLocalDateTime(
                            date = datePickerState.selectedDateMillis.selectedDateMillisToLocalDateTime().date,
                            hour = calculateFromFocusSessions(
                                focusSessions = focusSessions,
                                sessionTime = sessionTime,
                                shortBreakTime = shortBreakTime,
                                longBreakTime = longBreakTime,
                            ).hour,
                            minute = calculateFromFocusSessions(
                                focusSessions = focusSessions,
                                sessionTime = sessionTime,
                                shortBreakTime = shortBreakTime,
                                longBreakTime = longBreakTime,
                            ).minute,
                        ),
                        color = 0xFFAFBBF2,
                        current = 1,
                        date = datePickerState.selectedDateMillis.selectedDateMillisToLocalDateTime(),
                        focusSessions = focusSessions,
                        completed = true,
                        focusTime = sessionTime,
                        shortBreakTime = shortBreakTime,
                        longBreakTime = longBreakTime,
                        type = selectedOption,
                    ),
                )
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun AddTaskScreenContent(
    snackbarHostState: SnackbarHostState,
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
    onClickPickDate: () -> Unit,
    startTimePickerState: TimePickerState,
    datePickerState: DatePickerState,
) {
    Scaffold(
        snackbarHost = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter, // Change to your desired position
            ) {
                SnackbarHost(hostState = snackbarHostState)
            }
        },
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
