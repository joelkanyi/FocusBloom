package com.joelkanyi.focusbloom.presentation.statistics.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ChartBar(
    modifier: Modifier = Modifier,
    today: Boolean = false,
    value: Int,
    maxValue: Int,
    title: String,
) {
    /**
     * 1. Calculate the percentage of the value compared to the max value
     * 2. Multiply the percentage by the max width of the bar
     * 3. Set the height of the bar to the result
     * 4. Set the background color of the bar to the color
     * 5. Set the modifier of the bar to the modifier
     * 6. Clip the bar to a rounded corner shape
     * 7. Set the width of the bar to 8.dp
     */
    val height = (value.toFloat() / maxValue.toFloat()) * 100
    Column {
        Box(
            modifier = modifier
                .width(24.dp)
                .height(height.dp)
                .clip(shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
                .background(
                    color = if (today) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    },
                ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            modifier = Modifier,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun StatsChart(
    modifier: Modifier = Modifier,
    data: List<StatsChartData>,
) {
    val maxValue = 50

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = androidx.compose.ui.Alignment.Bottom,
    ) {
        data.forEach { chartData ->
            ChartBar(
                value = chartData.value,
                maxValue = maxValue,
                title = chartData.title,
                today = chartData.today,
            )
        }
    }
}

data class StatsChartData(
    val value: Int,
    val title: String,
    val color: Color,
    val today: Boolean = false,
)

val statsData = listOf(
    StatsChartData(
        value = 100,
        color = Red,
        title = "Mon",
    ),
    StatsChartData(
        value = 20,
        color = Green,
        title = "Tue",
    ),
    StatsChartData(
        value = 100,
        color = Yellow,
        title = "Wed",
    ),
    StatsChartData(
        value = 40,
        color = Blue,
        title = "Thu",
    ),
    StatsChartData(
        value = 50,
        color = Magenta,
        title = "Fri",
        today = true,
    ),
    StatsChartData(
        value = 60,
        color = Gray,
        title = "Sat",
    ),
    StatsChartData(
        value = 70,
        color = Red,
        title = "Sun",
    ),
)
