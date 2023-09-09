package com.joelkanyi.focusbloom.statistics.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.BarChartEntry
import io.github.koalaplot.core.bar.DefaultBarChartEntry
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.VerticalBarChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.VerticalRotation
import io.github.koalaplot.core.util.rotateVertically
import io.github.koalaplot.core.util.toString
import io.github.koalaplot.core.xychart.LinearAxisModel
import io.github.koalaplot.core.xychart.TickPosition
import io.github.koalaplot.core.xychart.XYChart
import io.github.koalaplot.core.xychart.rememberAxisStyle

internal val padding = 8.dp
internal val paddingMod = Modifier.padding(padding)

internal val fibonacci = mutableStateListOf(10.0f, 8.0f, 5.0f, 3.0f, 5.0f, 8.0f, 22.0f)

private const val DegreesHalfCircle: Float = 180.0f
private const val BarWidth = 0.8f
private val YAxisRange = 0f..fibonacci.maxOf { it }
private val XAxisRange = 0.5f..7.5f

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

@Composable
fun AxisTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        title,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier,
    )
}

@Composable
fun AxisLabel(label: String, modifier: Modifier = Modifier) {
    Text(
        label,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

fun barChartEntries(): List<BarChartEntry<Float, Float>> {
    return buildList {
        fibonacci.forEachIndexed { index, fl ->
            add(
                DefaultBarChartEntry(
                    xValue = (index + 1).toFloat(),
                    yMin = 0f,
                    yMax = fl,
                ),
            )
        }
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun BarSamplePlot(
    thumbnail: Boolean = false,
    tickPositionState: TickPositionState,
    title: String,
) {
    val barChartEntries = remember(thumbnail) { barChartEntries() }

    ChartLayout(
        modifier = paddingMod,
        title = { },
    ) {
        XYChart(
            xAxisModel = LinearAxisModel(
                XAxisRange,
                minimumMajorTickIncrement = 1f,
                minimumMajorTickSpacing = 10.dp,
                zoomRangeLimit = 3f,
                minorTickCount = 0,
            ),
            yAxisModel = LinearAxisModel(
                YAxisRange,
                minimumMajorTickIncrement = 1f,
                minorTickCount = 0,
            ),
            xAxisStyle = rememberAxisStyle(
                tickPosition = tickPositionState.horizontalAxis,
                color = Color.LightGray,
            ),
            xAxisLabels = {
                if (!thumbnail) {
                    AxisLabel(
                        when (it) {
                            1f -> "Mon"
                            2f -> "Tue"
                            3f -> "Wed"
                            4f -> "Thu"
                            5f -> "Fri"
                            6f -> "Sat"
                            7f -> "Sun"
                            else -> ""
                        },
                        Modifier.padding(top = 2.dp),
                    )
                }
            },
            xAxisTitle = {
                if (!thumbnail) {
                    AxisTitle(
                        "Day of the Week",
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            },
            yAxisStyle = rememberAxisStyle(tickPosition = tickPositionState.verticalAxis),
            yAxisLabels = {
                if (!thumbnail) AxisLabel(it.toString(0), Modifier.absolutePadding(right = 2.dp))
            },
            yAxisTitle = {
                if (!thumbnail) {
                    AxisTitle(
                        "Tasks Completed",
                        modifier = Modifier.rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
                            .padding(bottom = padding),
                    )
                }
            },
            verticalMajorGridLineStyle = null,
        ) {
            VerticalBarChart(
                series = listOf(barChartEntries),
                bar = { _, _, value ->
                    DefaultVerticalBar(
                        brush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth(BarWidth),
                    ) {
                        if (!thumbnail) {
                            HoverSurface { Text(value.yMax.toString()) }
                        }
                    }
                },

            )
        }
    }
}

data class TickPositionState(
    val verticalAxis: TickPosition,
    val horizontalAxis: TickPosition,
)

@Composable
fun HoverSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        shadowElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        color = Color.LightGray,
        modifier = modifier.padding(padding),
    ) {
        Box(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}
