package com.joelkanyi.focusbloom.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateListOf
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.joelkanyi.focusbloom.core.presentation.component.BloomDropDown
import com.joelkanyi.focusbloom.core.presentation.component.BloomInputTextField
import com.joelkanyi.focusbloom.core.presentation.component.BloomTopAppBar
import com.joelkanyi.focusbloom.domain.model.TextFieldState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val screenModel: SettingsScreenModel by inject()
        val state = screenModel.appTheme.collectAsState(
            initial = 2,
        )
        val navigator = LocalNavigator.currentOrThrow
        SettingsScreenContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent() {
    Scaffold(
        topBar = {
            BloomTopAppBar(
                hasBackNavigation = false,
            ) {
                Text(text = "Settings")
            }
        },
    ) { paddingValues ->
        val optionsOpened = mutableStateListOf("")

        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(settingsOption) { option ->
                SettingCard(
                    title = option.title,
                    icon = option.icon,
                    content = option.content,
                    expanded = optionsOpened.contains(option.title),
                    onExpand = {
                        if (optionsOpened.contains(option.title)) {
                            optionsOpened.remove(option.title)
                        } else {
                            optionsOpened.add(option.title)
                        }
                    },
                )
            }
        }
    }
}

data class Setting(
    val title: String,
    val icon: ImageVector,
    val content: @Composable () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
val settingsOption = listOf(
    Setting(
        title = "Focus Sessions",
        icon = Icons.Outlined.HourglassEmpty,
        content = {
            var focusSessionMinutes by remember { mutableStateOf("") }
            var shortBreakMinutes by remember { mutableStateOf("") }
            var longBreakMinutes by remember { mutableStateOf("") }
            var autoStartBreaks by remember { mutableStateOf(false) }
            var autoStartFocusSession by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SessionTime(
                    modifier = Modifier.weight(1f),
                    title = "Session",
                    currentValue = focusSessionMinutes,
                    onValueChange = {
                        focusSessionMinutes = it
                    },
                )
                SessionTime(
                    modifier = Modifier.weight(1f),
                    title = "Short Break",
                    currentValue = shortBreakMinutes,
                    onValueChange = {
                        shortBreakMinutes = it
                    },
                )
                SessionTime(
                    modifier = Modifier.weight(1f),
                    title = "Long Break",
                    currentValue = longBreakMinutes,
                    onValueChange = {
                        longBreakMinutes = it
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
    ),
    Setting(
        title = "Time",
        icon = Icons.Outlined.Timer,
        content = {
            var selectedHourFormat by remember {
                mutableStateOf("24-hour")
            }
            SoundSelection(
                title = "Hour Format",
                options = listOf("24-hour", "12-hour"),
                selectedOption = selectedHourFormat,
                onSelectOption = {
                    selectedHourFormat = it
                },
            )
        },
    ),
    Setting(
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
    ),
    Setting(
        title = "Theme",
        icon = Icons.Outlined.LightMode,
        content = {
            var showColorDialog by remember {
                mutableStateOf(false)
            }
            var selectedColorCard by remember {
                mutableStateOf("")
            }
            var darkTheme by remember {
                mutableStateOf(false)
            }
            if (showColorDialog) {
                ColorsDialog(
                    title = "Choose $selectedColorCard Color",
                    onDismiss = {
                        showColorDialog = false
                    },
                    onSelectColor = {
                        showColorDialog = false
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
                        showColorDialog = true
                        selectedColorCard = "Focus Session"
                    },
                    onSelectShortBreakColor = {
                        showColorDialog = true
                        selectedColorCard = "Short Break"
                    },
                    onSelectLongBreakColor = {
                        showColorDialog = true
                        selectedColorCard = "Long Break"
                    },
                    /*currentSessionColor = SessionColor,
                    currentShortBreakColor = ShortBreakColor,
                    currentLongBreakColor = LongBreakColor,*/
                    currentSessionColor = Color.Magenta,
                    currentShortBreakColor = Color.Cyan,
                    currentLongBreakColor = Color.Yellow,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            AutoStartSession(
                title = "App Theme (Light)",
                checked = darkTheme,
                onCheckedChange = {
                    darkTheme = it
                },
            )
        },
    ),
    Setting(
        title = "Notifications",
        icon = Icons.Outlined.Notifications,
        content = {
            var selectedReminderType by remember {
                mutableStateOf("Both")
            }
            var howManyMinutesToReminder by remember {
                mutableStateOf("5")
            }
            Row {
                Text(
                    modifier = Modifier.fillMaxWidth(.4f),
                    text = "Reminder",
                )
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                ) {
                    BloomDropDown(
                        options = listOf("Focus Session", "Break", "Both", "None"),
                        selectedOption = TextFieldState(selectedReminderType),
                        onOptionSelected = {
                            selectedReminderType = it
                        },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                    ) {
                        BloomInputTextField(
                            modifier = Modifier.weight(1f),
                            value = TextFieldState(text = howManyMinutesToReminder),
                            onValueChange = {
                                howManyMinutesToReminder = it
                            },
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "min")
                    }
                }
            }
        },
    ),
)

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
                onSelectOption.toString()
            },
        )
    }
}

@Composable
fun AutoStartSession(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
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

@Composable
fun SettingCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onExpand: () -> Unit,
    expanded: Boolean,
) {
    Card(modifier = modifier) {
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
    onSelectSessionColor: (Color) -> Unit,
    onSelectShortBreakColor: (Color) -> Unit,
    onSelectLongBreakColor: (Color) -> Unit,
    currentSessionColor: Color,
    currentShortBreakColor: Color,
    currentLongBreakColor: Color,
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
fun ColorCard(
    modifier: Modifier = Modifier,
    color: Color,
    onClick: (Color) -> Unit,
) {
    Box(
        modifier = modifier.size(32.dp).clip(MaterialTheme.shapes.medium).background(color)
            .clickable {
                onClick(color)
            },
    )
}

@Composable
fun ColorsDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSelectColor: (Color) -> Unit,
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
    Color.Magenta,
    Color.Cyan,
    Color.Yellow,
    Color.Red,
    Color.Green,
    Color.Blue,
    Color.Gray,
    Color.LightGray,
    Color.DarkGray,
    Color.Black,
    Color.White,
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
