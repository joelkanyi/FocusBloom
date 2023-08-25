package com.joelkanyi.focusbloom.android.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joelkanyi.focusbloom.android.R
import com.joelkanyi.focusbloom.android.component.BloomTopAppBar
import com.joelkanyi.focusbloom.android.ui.theme.FocusBloomTheme
import com.joelkanyi.horizontalcalendar.HorizontalCalendarView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun CalendarScreen(
    navigator: DestinationsNavigator,
) {
    CalendarScreenContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreenContent() {
    Scaffold(
        topBar = {
            BloomTopAppBar(
                hasBackNavigation = false,
            ) {
                Text(text = stringResource(R.string.schedule))
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                HorizontalCalendarView(
                    modifier = Modifier.fillMaxWidth(),
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unSelectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedCardColor = MaterialTheme.colorScheme.primary,
                    unSelectedCardColor = MaterialTheme.colorScheme.surfaceVariant,
                    onDayClick = { day ->
                        // Toast.makeText(context, day.toString(), Toast.LENGTH_SHORT).show()
                    },
                )
            }

            /**
             * I want a time bard that acts as a ruler i.e 4AM to 3AM
             */

            /**
             * I want a time bard that acts as a ruler i.e 4AM to 3AM
             */

            /**
             * I want a time bard that acts as a ruler i.e 4AM to 3AM
             */

            /**
             * I want a time bard that acts as a ruler i.e 4AM to 3AM
             */
            items(24) { hour ->
                CalendarTimeBand(
                    time = convertTheHourToTime(hour, 4),
                )
            }
        }
    }
}

fun convertTheHourToTime(hour: Int, startTime: Int): String {
    return when (hour) {
        0 -> "12AM"
        in 1..11 -> "${hour}AM"
        12 -> "12PM"
        in 13..23 -> "${hour - 12}PM"
        else -> "12AM"
    }
}

@Composable
fun Task(
    modifier: Modifier = Modifier,
    date: String,
    taskName: String,
    startTime: String,
    endTime: String,
) {
    Card {
        Row(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(.65f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            Text(
                text = date,
            )
            Text(
                text = taskName,
            )
        }
    }
}

@Composable
fun CalendarTimeBand(
    modifier: Modifier = Modifier,
    time: String,
) {
    Column {
        Spacer(modifier = modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = time)
            Divider()
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview
@Composable
fun CalendarTimeBandPreview() {
    FocusBloomTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.onPrimary),
        ) {
            CalendarTimeBand(
                time = "12:00 AM",
            )
        }
    }
}

@Preview
@Composable
fun CalendarScreenContentPreview() {
    FocusBloomTheme {
        CalendarScreenContent()
    }
}
