package com.konopelko.booksgoals.presentation.totalstatistics.ui

import android.annotation.SuppressLint
import android.graphics.Typeface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konopelko.booksgoals.R
import com.konopelko.booksgoals.presentation.common.theme.BookTrackerTheme
import com.konopelko.booksgoals.presentation.goalstatistics.model.ProgressMarkUiModel
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.MONTH
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.WEEK
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.YEAR
import com.konopelko.booksgoals.presentation.totalstatistics.TotalStatisticsIntent
import com.konopelko.booksgoals.presentation.totalstatistics.TotalStatisticsIntent.SelectedScaleChanged
import com.konopelko.booksgoals.presentation.totalstatistics.TotalStatisticsIntent.StatisticsTabChanged
import com.konopelko.booksgoals.presentation.totalstatistics.TotalStatisticsIntent.TotalStatisticsNavigationIntent
import com.konopelko.booksgoals.presentation.totalstatistics.TotalStatisticsUiState
import com.konopelko.booksgoals.presentation.totalstatistics.TotalStatisticsViewModel
import com.konopelko.booksgoals.presentation.totalstatistics.model.TotalStatisticsData
import com.konopelko.booksgoals.presentation.totalstatistics.model.TotalStatisticsTab
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer.ColumnProvider
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker.LabelPosition.AbovePoint
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarkerValueFormatter
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.component.LineComponent
import org.koin.androidx.compose.getViewModel
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Locale

@SuppressLint("ConstantLocale")
private val monthNames = DateFormatSymbols.getInstance(Locale.getDefault()).shortMonths

private val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
private val monthDays = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
private val weekDays = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

@Composable
fun TotalStatisticsScreen(
    viewModel: TotalStatisticsViewModel = getViewModel(),
    onNavigate: (TotalStatisticsNavigationIntent) -> Unit
) = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    val uiState by viewModel.uiState.collectAsState()

    TotalStatisticsContent(
        uiState = uiState,
        onIntent = viewModel::acceptIntent
    )
}

@Composable
private fun TotalStatisticsContent(
    uiState: TotalStatisticsUiState,
    onIntent: (TotalStatisticsIntent) -> Unit
) = Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    TotalStatisticsHeader()

    StatisticsTabsBar(
        selectedTab = uiState.selectedTab,
        onIntent = onIntent
    )

    Spacer(modifier = Modifier.weight(1f))

    StatisticsScaleMenu(
        selectedStatisticsScale = uiState.selectedScale,
        onIntent = onIntent
    )

    ColumnChartContent(
        scale = uiState.selectedScale,
        visibleMarks = uiState.visibleProgressMarks,
        initialScrollPosition = if (uiState.visibleProgressMarks.isNotEmpty()) {
            uiState.visibleProgressMarks.first().dateMark - 1f
        } else { 0f }
    )

    StatisticsInfoContent(uiState = uiState)

    Spacer(modifier = Modifier.weight(1f))
}

@Composable
private fun TotalStatisticsHeader() = Text(
    modifier = Modifier.padding(top = 16.dp),
    text = "Моя статистика",
    fontSize = 24.sp
)

@Composable
private fun StatisticsTabsBar(
    selectedTab: TotalStatisticsTab,
    onIntent: (TotalStatisticsIntent) -> Unit
) {
    val tabs = TotalStatisticsTab.entries

    TabRow(
        modifier = Modifier.padding(
            top = 24.dp,
            start = 16.dp,
            end = 16.dp
        ),
        selectedTabIndex = tabs.indexOf(selectedTab)
    ) {
        tabs.forEach {
            Tab(
                text = { Text(text = it.text) },
                selected = it == selectedTab,
                onClick = { onIntent(StatisticsTabChanged(it)) }
            )
        }
    }
}

@Composable
private fun StatisticsScaleMenu(
    selectedStatisticsScale: StatisticsScale,
    onIntent: (TotalStatisticsIntent) -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    horizontalArrangement = Arrangement.SpaceEvenly
) {
    Text(
        modifier = Modifier.clickable { onIntent(SelectedScaleChanged(WEEK)) },
        text = "Неделя",
        color = if (selectedStatisticsScale == WEEK) Color(0xFF4B6CA1)
        else Color.Black,
        textDecoration = if (selectedStatisticsScale == WEEK) TextDecoration.Underline
        else TextDecoration.None
    )
    Text(
        modifier = Modifier.clickable { onIntent(SelectedScaleChanged(MONTH)) },
        text = "Месяц",
        color = if (selectedStatisticsScale == MONTH) Color(0xFF4B6CA1)
        else Color.Black,
        textDecoration = if (selectedStatisticsScale == MONTH) TextDecoration.Underline
        else TextDecoration.None
    )
    Text(
        modifier = Modifier.clickable { onIntent(SelectedScaleChanged(YEAR)) },
        text = "Год",
        color = if (selectedStatisticsScale == YEAR) Color(0xFF4B6CA1)
        else Color.Black,
        textDecoration = if (selectedStatisticsScale == YEAR) TextDecoration.Underline
        else TextDecoration.None
    )
}

@Composable
private fun ColumnChartContent(
    scale: StatisticsScale,
    visibleMarks: List<ProgressMarkUiModel>,
    initialScrollPosition: Float
) = CartesianChartHost(
    modifier = Modifier
        .fillMaxHeight(0.5f)
        .padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 32.dp
        ),
    model = CartesianChartModel(
        ColumnCartesianLayerModel.build {
            series(
                x = calculateX(scale),
                y = calculateY(
                    progressMarks = visibleMarks,
                    statisticsScale = scale
                )
            )
        }
    ),
    chart = rememberCartesianChart(
        layers = arrayOf(
            rememberColumnCartesianLayer(
                columnProvider = ColumnProvider.series(
                    ChartColumn()
                ),
                spacing = 24.dp
            )
        ),
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(
            valueFormatter = setupBottomAxisValueFormatter(scale)
        )
    ),
    marker = rememberDefaultCartesianMarker(
        valueFormatter = DefaultCartesianMarkerValueFormatter(
            colorCode = false
        ),
        label = rememberTextComponent(
            color = Color.Black,
            textSize = 20.sp,
            typeface = Typeface.MONOSPACE,
            padding = Dimensions(0f, 0f, 0f, 8f)
        ),
        labelPosition = AbovePoint
    ),
    scrollState = rememberVicoScrollState(
        initialScroll = Scroll.Absolute.x(x = initialScrollPosition)
    )
)

@Composable
private fun StatisticsInfoContent(
    uiState: TotalStatisticsUiState
) = Column(
    modifier = Modifier.fillMaxWidth()
) {
    with(uiState.totalStatisticsData) {
        TotalStatisticsSection(
            statisticsTab = statisticsTab,
            statisticsScale = statisticsScale,
            totalPagesRead = totalUnitsRead,
            averageReadSpeed = averageReadSpeed,
            goalsAchieved = goalsAchieved
        )
    }
}

@Composable
private fun TotalStatisticsSection(
    statisticsTab: TotalStatisticsTab,
    statisticsScale: StatisticsScale,
    totalPagesRead: Int,
    averageReadSpeed: Int,
    goalsAchieved: Int
) = Column(
    modifier = Modifier.padding(
        top = 32.dp,
        start = 8.dp,
        end = 8.dp
    ),
) {
    Row(
        modifier = Modifier.padding(start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_pages),
            contentDescription = "",
            tint = Color(0xFF4B6CA1)
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = "Обзор ${getStatisticsInfoHeaderText(statisticsTab)}",
            style = MaterialTheme.typography.titleMedium
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$totalPagesRead",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Всего ${getStatisticsInfoHeaderText(statisticsTab)}")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$averageReadSpeed/${getAverageSpeedTextUnitPerScale(statisticsScale)}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Среднее")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$goalsAchieved",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Целей достигнуто"
            )
        }
    }
}

private fun getAverageSpeedTextUnitPerScale(statisticsScale: StatisticsScale): String =
    when (statisticsScale) {
        WEEK -> "день"
        MONTH -> "день"
        YEAR -> "месяц"
}

private fun getStatisticsInfoHeaderText(statisticsTab: TotalStatisticsTab): String =
    when (statisticsTab) {
        TotalStatisticsTab.PAGES -> "страниц"
        TotalStatisticsTab.BOOKS -> "книг"
    }

@Suppress("FunctionName")
private fun ChartColumn() = LineComponent(
    color = android.graphics.Color.parseColor("#88DEAE"),
    thicknessDp = 16.dp.value
)

//todo: move to viewmodel
private fun calculateX(statisticsScale: StatisticsScale): Collection<Number> =
    when (statisticsScale) {
        WEEK -> (1..7).toList()
        MONTH -> (1..monthDays).toList()
        YEAR -> (1..12).toList()
    }

//todo: move to viewmodel
private fun calculateY(
    progressMarks: List<ProgressMarkUiModel>,
    statisticsScale: StatisticsScale
): Collection<Number> = when (statisticsScale) {
    WEEK -> getYProgressValues(
        progressMarks = progressMarks,
        listSize = 7 // week days amount
    )
    MONTH -> getYProgressValues(
        progressMarks = progressMarks,
        listSize = monthDays
    )
    YEAR -> getYProgressValues(
        progressMarks = progressMarks,
        listSize = 12 // year month amount
    )
}

//todo: move to viewmodel
private fun getYProgressValues(
    progressMarks: List<ProgressMarkUiModel>,
    listSize: Int
): List<Int> = with(progressMarks.map { it.dateMark }) {
    List(size = listSize) { 0 }
        .mapIndexed { index, valueY ->
            if (contains(index + 1)) {
                progressMarks.firstOrNull { it.dateMark == index + 1 }?.progress ?: 0
            } else valueY
        }
}

//todo: move to viewmodel
private fun setupBottomAxisValueFormatter(statisticsScale: StatisticsScale): CartesianValueFormatter =
    CartesianValueFormatter { x, _, _ ->
        when (statisticsScale) {
            WEEK -> weekDays[x.toInt() - 1]
            MONTH -> "$currentMonth/${x.toInt()}"
            YEAR -> monthNames[x.toInt() % 12]
        }
    }

@Preview(showBackground = true)
@Composable
private fun TotalStatisticsScreenPreview() = BookTrackerTheme {
    TotalStatisticsContent(
        uiState = TotalStatisticsUiState(
            visibleProgressMarks = listOf(
                ProgressMarkUiModel(progress = 50, dateMark = 3),
                ProgressMarkUiModel(progress = 30, dateMark = 4),
                ProgressMarkUiModel(progress = 40, dateMark = 5),
                ProgressMarkUiModel(progress = 57, dateMark = 6),
                ProgressMarkUiModel(progress = 20, dateMark = 7),
            ),
            totalStatisticsData = TotalStatisticsData(
                statisticsTab = TotalStatisticsTab.PAGES,
                statisticsScale = MONTH,
                totalUnitsRead = 305,
                averageReadSpeed = 34,
                goalsAchieved = 3
            ),
            selectedScale = MONTH
        ),
        onIntent = {}
    )
}
