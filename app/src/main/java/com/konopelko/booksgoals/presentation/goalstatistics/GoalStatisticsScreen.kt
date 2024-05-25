package com.konopelko.booksgoals.presentation.goalstatistics

import android.annotation.SuppressLint
import android.graphics.Typeface
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.konopelko.booksgoals.R
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.common.theme.BooksGoalsAppTheme
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsIntent.ArgsReceived
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsIntent.GoalStatisticsNavigationIntent
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsIntent.SelectedScaleChanged
import com.konopelko.booksgoals.presentation.goalstatistics.model.ProgressMarkUiModel
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.MONTH
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.WEEK
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.YEAR
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer.ColumnProvider
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker.LabelPosition
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

private const val ARGS_KEY = "args_key"

@Composable
fun GoalStatisticsScreen(
    viewModel: GoalStatisticsViewModel = getViewModel(),
    onNavigate: (GoalStatisticsNavigationIntent) -> Unit,
    args: Int?
) = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    val uiState by viewModel.uiState.collectAsState()

    args?.let {
        LaunchedEffect(ARGS_KEY) {
            viewModel.acceptIntent(ArgsReceived(it))
        }
    }

    GoalStatisticsContent(
        uiState = uiState,
        onIntent = viewModel::acceptIntent
    )
}

@Composable
private fun GoalStatisticsContent(
    uiState: GoalStatisticsUiState,
    onIntent: (GoalStatisticsIntent) -> Unit
) = Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
){
    StatisticsHeader()

    BookInfoContent(uiState.book)

    Spacer(modifier = Modifier.height(32.dp))

    StatisticsScaleMenu(
        selectedStatisticsScale = uiState.selectedStatisticsScale,
        onIntent = onIntent
    )

    ColumnChartContent(
        scale = uiState.selectedStatisticsScale,
        visibleMarks = uiState.visibleProgressMarks
    )

    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
private fun StatisticsHeader() = Text(
    modifier = Modifier.padding(
        top = 16.dp,
        start = 16.dp
    ),
    text = "Статистика книги",
    fontSize = 24.sp
)

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun BookInfoContent(book: Book) = Column(
    modifier = Modifier.padding(top = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    if(book.coverUrl.isNotEmpty()) {
        GlideImage(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .fillMaxHeight(0.25f),
            model = book.coverUrl,
            contentDescription = "Book image",
            loading = placeholder(R.drawable.ic_default_book),
            failure = placeholder(R.drawable.ic_default_book)
        )
    } else {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .fillMaxHeight(0.25f),
            painter = painterResource(id = R.drawable.ic_default_book),
            contentDescription = ""
        )
    }

    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = book.title,
        fontSize = 24.sp
    )
    Text(text = "${book.authorName} - ${book.publishYear}")
}

@Composable
private fun ColumnChartContent(
    scale: StatisticsScale,
    visibleMarks: List<ProgressMarkUiModel>
) = CartesianChartHost(
    modifier = Modifier
        .fillMaxHeight()
        .padding(
            top = 16.dp,
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
        labelPosition = LabelPosition.AbovePoint
    )
)

@Composable
private fun StatisticsScaleMenu(
    selectedStatisticsScale: StatisticsScale,
    onIntent: (GoalStatisticsIntent) -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    horizontalArrangement = Arrangement.SpaceEvenly
) {
    Text(
        modifier = Modifier.clickable {
            onIntent(SelectedScaleChanged(WEEK))
        },
        text = "Неделя",
        color = if (selectedStatisticsScale == WEEK) Color(0xFF4B6CA1)
        else Color.Black,
        textDecoration = if (selectedStatisticsScale == WEEK) TextDecoration.Underline
        else TextDecoration.None
    )
    Text(
        modifier = Modifier.clickable {
            onIntent(SelectedScaleChanged(MONTH))
        },
        text = "Месяц",
        color = if (selectedStatisticsScale == MONTH) Color(0xFF4B6CA1)
        else Color.Black,
        textDecoration = if (selectedStatisticsScale == MONTH) TextDecoration.Underline
        else TextDecoration.None
    )
    Text(
        modifier = Modifier.clickable {
            onIntent(SelectedScaleChanged(YEAR))
        },
        text = "Год",
        color = if (selectedStatisticsScale == YEAR) Color(0xFF4B6CA1)
        else Color.Black,
        textDecoration = if (selectedStatisticsScale == YEAR) TextDecoration.Underline
        else TextDecoration.None
    )
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
private fun GoalStatisticsPreview() = BooksGoalsAppTheme {
    GoalStatisticsContent(
        uiState = GoalStatisticsUiState(
            book = Book(
                title = "Война и мир",
                authorName = "Лев Николаевич Толстой",
                publishYear = "1869"
            ),
            visibleProgressMarks = listOf(
                ProgressMarkUiModel(progress = 50, dateMark = 3),
                ProgressMarkUiModel(progress = 30, dateMark = 4),
                ProgressMarkUiModel(progress = 40, dateMark = 5),
                ProgressMarkUiModel(progress = 57, dateMark = 6),
                ProgressMarkUiModel(progress = 20, dateMark = 7),
            )
        ),
        onIntent = {}
    )
}