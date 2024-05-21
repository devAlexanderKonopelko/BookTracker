package com.konopelko.booksgoals.presentation.goalstatistics

import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konopelko.booksgoals.presentation.common.theme.BooksGoalsAppTheme
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer.ColumnProvider
import com.patrykandpatrick.vico.core.common.component.LineComponent
import java.text.DateFormatSymbols
import java.util.Locale

private val monthNames = DateFormatSymbols.getInstance(Locale.US).shortMonths

@Composable
fun GoalStatisticsScreen() = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center
) {
    Text(text = "Goal Statistics")

    val scrollState = rememberVicoScrollState()

//    val modelProducer = remember {
//        CartesianChartModelProducer.build {
//
//        }
//    }

    CartesianChartHost(
        modifier = Modifier.fillMaxHeight(0.5f),
        model = CartesianChartModel(
            ColumnCartesianLayerModel.build {
                series(y = listOf(7, 15, 4, 8, 19, 4, 21, 5, 30, 21))
            }
        ),
        chart = rememberCartesianChart(
            layers = arrayOf(
                rememberColumnCartesianLayer(
                    columnProvider = ColumnProvider.series(
                        ChartColumn()
                    ),
                    spacing = 3.dp
                )
            ),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(
                valueFormatter = bottomAxisValueFormatter,
                itemPlacer = remember {
                    AxisItemPlacer.Horizontal.default(
                        spacing = 3
                    )
                },
            )
        ),
        scrollState = scrollState
    )
}

@Suppress("FunctionName")
private fun ChartColumn() = LineComponent(
    color = Color.parseColor("#88DEAE"),
    thicknessDp = 8.dp.value
)

private val bottomAxisValueFormatter =
    CartesianValueFormatter { x, _, _ -> "${monthNames[x.toInt() % 12]} ’${20 + x.toInt() / 12}" }

@Preview(showBackground = true)
@Composable
private fun GoalStatisticsPreview() = BooksGoalsAppTheme {
    GoalStatisticsScreen()
}