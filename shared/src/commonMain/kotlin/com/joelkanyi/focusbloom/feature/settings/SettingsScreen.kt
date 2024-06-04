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
package com.joelkanyi.focusbloom.feature.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.joelkanyi.focusbloom.core.domain.model.TextFieldState
import com.joelkanyi.focusbloom.core.presentation.component.BloomDropDown
import com.joelkanyi.focusbloom.core.presentation.component.BloomInputTextField
import com.joelkanyi.focusbloom.core.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.core.presentation.theme.Blue
import com.joelkanyi.focusbloom.core.presentation.theme.Green
import com.joelkanyi.focusbloom.core.presentation.theme.LightBlue
import com.joelkanyi.focusbloom.core.presentation.theme.LightGreen
import com.joelkanyi.focusbloom.core.presentation.theme.LongBreakColor
import com.joelkanyi.focusbloom.core.presentation.theme.Orange
import com.joelkanyi.focusbloom.core.presentation.theme.Pink
import com.joelkanyi.focusbloom.core.presentation.theme.Red
import com.joelkanyi.focusbloom.core.presentation.theme.SessionColor
import com.joelkanyi.focusbloom.core.presentation.theme.ShortBreakColor
import com.joelkanyi.focusbloom.core.presentation.theme.Yellow
import com.joelkanyi.focusbloom.core.utils.isDigitsOnly
import com.joelkanyi.focusbloom.core.utils.timeFormat
import com.joelkanyi.focusbloom.platform.StatusBarColors
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    screenModel: SettingsScreenModel = koinInject(),
) {
    val darkTheme = when (screenModel.appTheme.collectAsState().value) {
        1 -> true
        else -> false
    }
    StatusBarColors(
        statusBarColor = MaterialTheme.colorScheme.background,
        navBarColor = MaterialTheme.colorScheme.background,
    )

    val sessionTime = screenModel.sessionTime.collectAsState().value ?: 25
    val shortBreakTime = screenModel.shortBreakTime.collectAsState().value ?: 5
    val longBreakTime = screenModel.longBreakTime.collectAsState().value ?: 15
    val timeFormat = screenModel.timeFormat.collectAsState().value ?: 24
    val selectedColorCardTitle = screenModel.selectedColorCardTitle.collectAsState().value
    val currentShortBreakColor = screenModel.shortBreakColor.collectAsState().value
    val currentLongBreakColor = screenModel.longBreakColor.collectAsState().value
    val currentSessionColor = screenModel.focusColor.collectAsState().value
    val showColorDialog = screenModel.showColorDialog.collectAsState().value
    val remindersOn = screenModel.remindersOn.collectAsState().value

    SettingsScreenContent(
        darkTheme = darkTheme,
        onDarkThemeChange = { themeValue ->
            screenModel.setAppTheme(if (themeValue) 1 else 0)
        },
        optionsOpened = screenModel.optionsOpened,
        openOptions = { option ->
            screenModel.openOptions(option)
        },
        focusSessionMinutes = sessionTime,
        onFocusSessionMinutesChange = { time ->
            if (time.isEmpty()) {
                screenModel.setSessionTime(0)
                return@SettingsScreenContent
            }
            if (time.isDigitsOnly().not()) {
                return@SettingsScreenContent
            }
            screenModel.setSessionTime(time.toInt())
        },
        shortBreakMinutes = shortBreakTime,
        onShortBreakMinutesChange = { time ->
            if (time.isEmpty()) {
                screenModel.setShortBreakTime(0)
                return@SettingsScreenContent
            }
            if (time.isDigitsOnly().not()) {
                return@SettingsScreenContent
            }
            screenModel.setShortBreakTime(time.toInt())
        },
        longBreakMinutes = longBreakTime,
        onLongBreakMinutesChange = { time ->
            if (time.isEmpty()) {
                screenModel.setLongBreakTime(0)
                return@SettingsScreenContent
            }
            if (time.isDigitsOnly().not()) {
                return@SettingsScreenContent
            }
            screenModel.setLongBreakTime(time.toInt())
        },
        hourFormats = screenModel.hourFormats,
        selectedHourFormat = timeFormat,
        onHourFormatChange = {
            screenModel.setHourFormat(it)
        },
        showColorDialog = showColorDialog,
        selectedColorCardTitle = selectedColorCardTitle,
        onColorCardTitleChange = {
            screenModel.setSelectedColorCardTitle(it)
        },
        onShowColorDialog = {
            screenModel.setShowColorDialog(it)
        },
        currentShortBreakColor = if (currentShortBreakColor?.toInt() == 0 || currentShortBreakColor == null) {
            ShortBreakColor
        } else {
            currentShortBreakColor
        },
        currentLongBreakColor = if (currentLongBreakColor?.toInt() == 0 || currentLongBreakColor == null) {
            LongBreakColor
        } else {
            currentLongBreakColor
        },
        currentSessionColor = if (currentSessionColor?.toInt() == 0 || currentSessionColor == null) {
            SessionColor
        } else {
            currentSessionColor
        },
        onSelectColor = {
            when (selectedColorCardTitle) {
                "Focus Session" -> {
                    screenModel.setFocusColor(it)
                }

                "Short Break" -> {
                    screenModel.setShortBreakColor(it)
                }

                "Long Break" -> {
                    screenModel.setLongBreakColor(it)
                }
            }
        },
        remindersOn = remindersOn == 1,
        onRemindersChange = {
            screenModel.setReminders(
                if (it) {
                    1
                } else {
                    0
                },
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    optionsOpened: List<String>,
    openOptions: (String) -> Unit,
    focusSessionMinutes: Int,
    onFocusSessionMinutesChange: (String) -> Unit,
    shortBreakMinutes: Int,
    onShortBreakMinutesChange: (String) -> Unit,
    longBreakMinutes: Int,
    onLongBreakMinutesChange: (String) -> Unit,
    hourFormats: List<String>,
    selectedHourFormat: Int,
    onHourFormatChange: (Int) -> Unit,
    showColorDialog: Boolean,
    selectedColorCardTitle: String,
    onColorCardTitleChange: (String) -> Unit,
    onShowColorDialog: (Boolean) -> Unit,
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    currentShortBreakColor: Long,
    currentLongBreakColor: Long,
    currentSessionColor: Long,
    onSelectColor: (Long) -> Unit,
    remindersOn: Boolean,
    onRemindersChange: (Boolean) -> Unit,
) {
    Scaffold(
        topBar = {
            BloomTopAppBar(
                hasBackNavigation = false,
            ) {
                Text(text = "Settings")
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                FocusSessionsSetting(
                    focusSessionMinutes = focusSessionMinutes,
                    onFocusSessionMinutesChange = onFocusSessionMinutesChange,
                    shortBreakMinutes = shortBreakMinutes,
                    onShortBreakMinutesChange = onShortBreakMinutesChange,
                    longBreakMinutes = longBreakMinutes,
                    onLongBreakMinutesChange = onLongBreakMinutesChange,
                    expanded = { title ->
                        optionsOpened.contains(title)
                    },
                    onExpand = { title ->
                        openOptions(title)
                    },
                )
            }
            item {
                TimeSetting(
                    expanded = { title ->
                        optionsOpened.contains(title)
                    },
                    onExpand = { title ->
                        openOptions(title)
                    },
                    hourFormats = hourFormats,
                    selectedHourFormat = selectedHourFormat,
                    onHourFormatChange = onHourFormatChange,
                )
            }
            /*item {
                SoundSetting(
                    expanded = { title ->
                        optionsOpened.contains(title)
                    },
                    onExpand = { title ->
                        openOptions(title)
                    }
                )
            }*/
            item {
                ThemeSetting(
                    expanded = { title ->
                        optionsOpened.contains(title)
                    },
                    onExpand = { title ->
                        openOptions(title)
                    },
                    showColorDialog = showColorDialog,
                    selectedColorCardTitle = selectedColorCardTitle,
                    onColorCardTitleChange = onColorCardTitleChange,
                    onShowColorDialog = onShowColorDialog,
                    darkTheme = darkTheme,
                    onDarkThemeChange = onDarkThemeChange,
                    currentShortBreakColor = currentShortBreakColor,
                    currentLongBreakColor = currentLongBreakColor,
                    currentSessionColor = currentSessionColor,
                    onSelectColor = onSelectColor,
                )
            }
            item {
                NotificationsSetting(
                    expanded = { title ->
                        optionsOpened.contains(title)
                    },
                    onExpand = { title ->
                        openOptions(title)
                    },
                    remindersOn = remindersOn,
                    onRemindersChange = onRemindersChange,
                )
            }
        }
    }
}

@Composable
fun FocusSessionsSetting(
    focusSessionMinutes: Int,
    onFocusSessionMinutesChange: (String) -> Unit,
    shortBreakMinutes: Int,
    onShortBreakMinutesChange: (String) -> Unit,
    longBreakMinutes: Int,
    onLongBreakMinutesChange: (String) -> Unit,
    onExpand: (String) -> Unit,
    expanded: (String) -> Boolean,
) {
    SettingCard(
        onExpand = {
            onExpand("Focus Sessions")
        },
        expanded = expanded("Focus Sessions"),
        title = "Focus Sessions",
        icon = Icons.Outlined.HourglassEmpty,
        content = {
            var autoStartBreaks by remember { mutableStateOf(false) }
            var autoStartFocusSession by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SessionTime(
                    modifier = Modifier.weight(1f),
                    title = "Session",
                    currentValue = focusSessionMinutes.toString(),
                    onValueChange = {
                        onFocusSessionMinutesChange(it)
                    },
                )
                SessionTime(
                    modifier = Modifier.weight(1f),
                    title = "Short Break",
                    currentValue = shortBreakMinutes.toString(),
                    onValueChange = {
                        onShortBreakMinutesChange(it)
                    },
                )
                SessionTime(
                    modifier = Modifier.weight(1f),
                    title = "Long Break",
                    currentValue = longBreakMinutes.toString(),
                    onValueChange = {
                        onLongBreakMinutesChange(it)
                    },
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            AutoStartSession(
                title = "Auto Start Breaks",
                checked = autoStartBreaks,
                onCheckedChange = {
                    autoStartBreaks = it
                },
            )
            Spacer(modifier = Modifier.height(12.dp))
            AutoStartSession(
                title = "Auto Start Sessions",
                checked = autoStartFocusSession,
                onCheckedChange = {
                    autoStartFocusSession = it
                },
            )
        },
    )
}

@Composable
fun TimeSetting(
    onExpand: (String) -> Unit,
    expanded: (String) -> Boolean,
    hourFormats: List<String>,
    selectedHourFormat: Int,
    onHourFormatChange: (Int) -> Unit,
) {
    SettingCard(
        onExpand = {
            onExpand("Time")
        },
        expanded = expanded("Time"),
        title = "Time",
        icon = Icons.Outlined.Timer,
        content = {
            SoundSelection(
                title = "Hour Format",
                options = hourFormats,
                selectedOption = selectedHourFormat.timeFormat(),
                onSelectOption = {
                    onHourFormatChange(it.timeFormat())
                    onExpand("Time")
                },
            )
        },
    )
}

@Composable
fun SoundSetting(onExpand: (String) -> Unit, expanded: (String) -> Boolean) {
    SettingCard(
        onExpand = {
            onExpand("Sounds")
        },
        expanded = expanded("Sounds"),
        title = "Sounds",
        icon = Icons.Outlined.VolumeUp,
        content = {
            var alarmSliderPosition by remember { mutableStateOf(0f) }
            var tickingSliderPosition by remember { mutableStateOf(0f) }
            var selectedAlarmSound by remember {
                mutableStateOf("Nokia Tune")
            }
            var selectedTickingSound by remember {
                mutableStateOf("White Noise")
            }
            SoundSelection(
                title = "Alarm Sounds",
                options = listOf("Nokia Tune", "Samsung Tune", "Itel Tune", "Oppo Tune"),
                selectedOption = selectedAlarmSound,
                onSelectOption = {
                    selectedAlarmSound = it
                },
            )
            Slider(
                value = alarmSliderPosition,
                valueRange = 0f..100f,
                onValueChange = { alarmSliderPosition = it },
                colors = SliderDefaults.colors(
                    inactiveTickColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondary,
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            SoundSelection(
                title = "Ticking Sounds",
                options = listOf("White Noise", "Clock Ticking"),
                selectedOption = selectedTickingSound,
                onSelectOption = {
                    selectedTickingSound = it
                },
            )
            Slider(
                value = tickingSliderPosition,
                valueRange = 0f..100f,
                onValueChange = { tickingSliderPosition = it },
                colors = SliderDefaults.colors(
                    inactiveTickColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondary,
                ),
            )
        },
    )
}

@Composable
fun ThemeSetting(
    onExpand: (String) -> Unit,
    expanded: (String) -> Boolean,
    showColorDialog: Boolean,
    selectedColorCardTitle: String,
    onColorCardTitleChange: (String) -> Unit,
    onShowColorDialog: (Boolean) -> Unit,
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    currentShortBreakColor: Long,
    currentLongBreakColor: Long,
    currentSessionColor: Long,
    onSelectColor: (Long) -> Unit,
) {
    SettingCard(
        onExpand = {
            onExpand("Theme")
        },
        expanded = expanded("Theme"),
        title = "Theme",
        icon = Icons.Outlined.LightMode,
        content = {
            if (showColorDialog) {
                ColorsDialog(
                    title = "Choose $selectedColorCardTitle Color",
                    onDismiss = {
                        onShowColorDialog(false)
                    },
                    onSelectColor = {
                        onShowColorDialog(false)
                        onSelectColor(it)
                    },
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Sessions Color Scheme")
                ColorsSelection(
                    onSelectSessionColor = {
                        onShowColorDialog(true)
                        onColorCardTitleChange("Focus Session")
                    },
                    onSelectShortBreakColor = {
                        onShowColorDialog(true)
                        onColorCardTitleChange("Short Break")
                    },
                    onSelectLongBreakColor = {
                        onShowColorDialog(true)
                        onColorCardTitleChange("Long Break")
                    },
                    currentSessionColor = currentSessionColor,
                    currentShortBreakColor = currentShortBreakColor,
                    currentLongBreakColor = currentLongBreakColor,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            AutoStartSession(
                title = "App Theme (${
                if (darkTheme) {
                    "Dark"
                } else {
                    "Light"
                }
                })",
                checked = darkTheme,
                onCheckedChange = {
                    onDarkThemeChange(it)
                },
            )
        },
    )
}

@Composable
fun NotificationsSetting(
    onExpand: (String) -> Unit,
    expanded: (String) -> Boolean,
    remindersOn: Boolean,
    onRemindersChange: (Boolean) -> Unit,
) {
    SettingCard(
        onExpand = {
            onExpand("Notifications")
        },
        expanded = expanded("Notifications"),
        title = "Notifications",
        icon = Icons.Outlined.Notifications,
        content = {
            AutoStartSession(
                title = "Reminders",
                checked = remindersOn,
                onCheckedChange = onRemindersChange,
            )
        },
    )
}

@Composable
private fun SoundSelection(
    options: List<String>,
    title: String,
    selectedOption: String,
    onSelectOption: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(.5f),
            text = title,
        )
        BloomDropDown(
            modifier = Modifier
                .fillMaxWidth(),
            options = options,
            selectedOption = TextFieldState(text = selectedOption),
            onOptionSelected = {
                onSelectOption(it)
            },
        )
    }
}

@Composable
fun AutoStartSession(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
fun SessionTime(
    modifier: Modifier = Modifier,
    title: String,
    currentValue: String,
    onValueChange: (String) -> Unit,
) {
    BloomInputTextField(
        modifier = modifier,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Start,
        ),
        label = {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        },
        value = TextFieldState(currentValue),
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onExpand: () -> Unit,
    expanded: Boolean,
) {
    Card(
        modifier = modifier,
        onClick = {
            onExpand()
        },
    ) {
        Column(
            modifier = modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }

                IconButton(onClick = { onExpand() }) {
                    Icon(
                        imageVector = if (expanded) {
                            Icons.Rounded.KeyboardArrowUp
                        } else {
                            Icons.Rounded.KeyboardArrowDown
                        },
                        contentDescription = null,
                    )
                }
            }
            AnimatedVisibility(expanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    content()
                }
            }
        }
    }
}

@Composable
fun ColorsSelection(
    onSelectSessionColor: (Long) -> Unit,
    onSelectShortBreakColor: (Long) -> Unit,
    onSelectLongBreakColor: (Long) -> Unit,
    currentSessionColor: Long,
    currentShortBreakColor: Long,
    currentLongBreakColor: Long,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ColorCard(
            color = currentSessionColor,
            onClick = onSelectSessionColor,
        )
        ColorCard(
            color = currentShortBreakColor,
            onClick = onSelectShortBreakColor,
        )
        ColorCard(
            color = currentLongBreakColor,
            onClick = onSelectLongBreakColor,
        )
    }
}

@Composable
fun ColorCard(modifier: Modifier = Modifier, color: Long, onClick: (Long) -> Unit) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color(color))
            .clickable {
                onClick(color)
            },
    )
}

@Composable
fun ColorsDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSelectColor: (Long) -> Unit,
    title: String,
) {
    AlertDialog(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        icon = {},
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = onDismiss,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    textAlign = TextAlign.Center,
                ),
            )
        },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(sessionColors) {
                    ColorCard(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(48.dp),
                        color = it,
                        onClick = onSelectColor,
                    )
                }
            }
        },
        dismissButton = {},
        confirmButton = {},
    )
}

private val sessionColors = listOf(
    SessionColor,
    ShortBreakColor,
    LongBreakColor,
    Red,
    Green,
    Orange,
    Blue,
    Green,
    LightGreen,
    Yellow,
    LightBlue,
    Pink,
)

/**
 * Settings
 * Focus Sessions
 *  - time for short, long and focus session
 *  - auto start breaks, sessions
 * Sounds
 *  - alarm sound - repeat times
 *  - ticking sound
 * Theme
 *  - for breaks and focus session
 * Hour format
 * Notification
 *  - reminder - last, middle task
 *  - how many minutes to
 *  - alarm name
 *  - add new one
 */
