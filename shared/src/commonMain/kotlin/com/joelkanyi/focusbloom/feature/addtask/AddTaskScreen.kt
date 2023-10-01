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
package com.joelkanyi.focusbloom.feature.addtask

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.model.TaskType
import com.joelkanyi.focusbloom.core.domain.model.TextFieldState
import com.joelkanyi.focusbloom.core.domain.model.taskTypes
import com.joelkanyi.focusbloom.core.presentation.component.BloomButton
import com.joelkanyi.focusbloom.core.presentation.component.BloomDateBoxField
import com.joelkanyi.focusbloom.core.presentation.component.BloomDropDown
import com.joelkanyi.focusbloom.core.presentation.component.BloomIncrementer
import com.joelkanyi.focusbloom.core.presentation.component.BloomInputTextField
import com.joelkanyi.focusbloom.core.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.core.presentation.theme.SuccessColor
import com.joelkanyi.focusbloom.core.utils.UiEvents
import com.joelkanyi.focusbloom.core.utils.calculateFromFocusSessions
import com.joelkanyi.focusbloom.core.utils.formattedTimeBasedOnTimeFormat
import com.joelkanyi.focusbloom.core.utils.selectedDateMillisToLocalDateTime
import com.joelkanyi.focusbloom.core.utils.toLocalDateTime
import com.joelkanyi.focusbloom.core.utils.today
import com.joelkanyi.focusbloom.platform.StatusBarColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.rememberKoinInject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddTaskScreen() {
    val screenModel: AddTaskScreenModel = rememberKoinInject()

    StatusBarColors(
        statusBarColor = MaterialTheme.colorScheme.background,
        navBarColor = MaterialTheme.colorScheme.background,
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val navigator = LocalNavigator.currentOrThrow
    val sessionTime = screenModel.sessionTime.collectAsState().value ?: 25
    val shortBreakTime = screenModel.shortBreakTime.collectAsState().value ?: 5
    val longBreakTime = screenModel.longBreakTime.collectAsState().value ?: 15
    val hourFormat = screenModel.hourFormat.collectAsState().value ?: 24
    val focusSessions = screenModel.focusSessions.collectAsState().value
    val showStartTimeInputDialog = screenModel.showStartTimeInputDialog.collectAsState().value
    val showTaskDatePickerDialog = screenModel.showTaskDatePickerDialog.collectAsState().value
    val selectedTaskType = screenModel.selectedOption.collectAsState().value
    val taskName = screenModel.taskName.value
    val taskDescription = screenModel.taskDescription.value

    val startTimeState = rememberTimePickerState(
        initialHour = today().hour,
        initialMinute = today().minute,
        is24Hour = hourFormat == 24,
    )
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds(),
    )
    val calculatedFocusTime by remember {
        mutableStateOf(
            calculateFromFocusSessions(
                focusSessions = focusSessions,
                sessionTime = sessionTime,
                shortBreakTime = shortBreakTime,
                longBreakTime = longBreakTime,
                currentLocalDateTime = LocalDateTime(
                    year = datePickerState.selectedDateMillis.selectedDateMillisToLocalDateTime().year,
                    month = datePickerState.selectedDateMillis.selectedDateMillisToLocalDateTime().month,
                    dayOfMonth = datePickerState.selectedDateMillis.selectedDateMillisToLocalDateTime().dayOfMonth,
                    hour = startTimeState.hour,
                    minute = startTimeState.minute,
                ),
            ),
        )
    }

    LaunchedEffect(key1 = true) {
        screenModel.eventsFlow.collectLatest { event ->
            when (event) {
                is UiEvents.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                    )
                    datePickerState.setSelection(
                        Clock.System.now().toEpochMilliseconds(),
                    )
                }

                UiEvents.NavigateBack -> {
                    navigator.pop()
                }

                else -> {}
            }
        }
    }

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
        hourFormat = hourFormat,
        calculatedFocusTime = calculatedFocusTime,
        taskOptions = taskTypes,
        selectedTaskType = selectedTaskType,
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
        onSelectedTaskTypeChange = {
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
                    /*end = toLocalDateTime(
                        date = datePickerState.selectedDateMillis.selectedDateMillisToLocalDateTime().date,
                        hour = calculatedFocusTime.hour,
                        minute = calculatedFocusTime.minute,
                    ),*/
                    color = selectedTaskType.color,
                    current = "Focus",
                    date = datePickerState.selectedDateMillis.selectedDateMillisToLocalDateTime(),
                    focusSessions = focusSessions,
                    completed = false,
                    /*focusTime = sessionTime.toMillis(),
                    shortBreakTime = shortBreakTime.toMillis(),
                    longBreakTime = longBreakTime.toMillis(),*/
                    type = selectedTaskType.name,
                    consumedFocusTime = 0L,
                    consumedShortBreakTime = 0L,
                    consumedLongBreakTime = 0L,
                    inProgressTask = false,
                    currentCycle = 0,
                    active = false,
                ),
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun AddTaskScreenContent(
    snackbarHostState: SnackbarHostState,
    calculatedFocusTime: LocalTime,
    hourFormat: Int,
    taskOptions: List<TaskType>,
    selectedTaskType: TaskType,
    onSelectedTaskTypeChange: (TaskType) -> Unit,
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
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                },
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                            ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(.85f),
                                    text = it.visuals.message,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                    ),
                                )
                                Image(
                                    modifier = Modifier
                                        .size(32.dp),
                                    painter = painterResource("ic_complete.xml"),
                                    contentDescription = "Task Options",
                                )
                            }
                        }
                    },
                )
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
                    maxLines = 3,
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
                    maxLines = 5,
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
                    selectedOption = TextFieldState(selectedTaskType.name),
                    onOptionSelected = onSelectedTaskTypeChange,
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TimeComponent(
                        time = LocalTime(
                            startTimePickerState.hour,
                            startTimePickerState.minute,
                        ),
                        hourFormat = hourFormat,
                        title = "Start Time",
                        icon = "start_time.xml",
                        iconColor = MaterialTheme.colorScheme.primary,
                        iconSize = 24,
                        onClick = onClickPickStartTime,
                    )

                    DashedDivider(
                        color = MaterialTheme.colorScheme.primary,
                        thickness = 3.dp,
                        phase = 5f,
                        modifier = Modifier
                            .width(180.dp),
                    )

                    TimeComponent(
                        time = calculatedFocusTime,
                        hourFormat = hourFormat,
                        title = "End Time",
                        icon = "end_time.xml",
                        iconColor = SuccessColor,
                        onClick = {},
                    )
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
                        Text(text = "Save")
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun TimeComponent(
    title: String,
    icon: String,
    iconColor: Color,
    iconSize: Int = 32,
    time: LocalTime,
    hourFormat: Int,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable {
            onClick()
        },
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = CenterVertically,
        ) {
            Text(
                text = time.formattedTimeBasedOnTimeFormat(hourFormat),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
            )

            Icon(
                modifier = Modifier
                    .size(iconSize.dp),
                painter = painterResource(icon),
                contentDescription = title,
                tint = iconColor,
            )
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

@Composable
fun DashedDivider(
    thickness: Dp,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    phase: Float = 10f,
    intervals: FloatArray = floatArrayOf(10f, 10f),
    modifier: Modifier,
) {
    Canvas(
        modifier = modifier,
    ) {
        val dividerHeight = thickness.toPx()
        drawRoundRect(
            color = color,
            style = Stroke(
                width = dividerHeight,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = intervals,
                    phase = phase,
                ),
            ),
        )
    }
}
