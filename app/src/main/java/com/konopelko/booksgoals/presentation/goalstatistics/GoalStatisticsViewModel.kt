package com.konopelko.booksgoals.presentation.goalstatistics

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.model.progress.ProgressMark
import com.konopelko.booksgoals.domain.usecase.getgoal.GetGoalUseCase
import com.konopelko.booksgoals.domain.usecase.getgoalprogress.GetGoalProgressUseCase
import com.konopelko.booksgoals.presentation.common.base.BaseViewModel
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsIntent.ArgsReceived
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsIntent.SelectedScaleChanged
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsUiState.PartialState
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsUiState.PartialState.BookLoaded
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsUiState.PartialState.ProgressMarksLoaded
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsUiState.PartialState.StatisticsScaleChanged
import com.konopelko.booksgoals.presentation.goalstatistics.model.ProgressMarkUiModel
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.MONTH
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.WEEK
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.YEAR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class GoalStatisticsViewModel(
    initialState: GoalStatisticsUiState,
    private val getGoalProgressUseCase: GetGoalProgressUseCase,
    private val getGoalUseCase: GetGoalUseCase
) : BaseViewModel<GoalStatisticsIntent, GoalStatisticsUiState, PartialState>(initialState) {

    private var goalFullProgressList: List<ProgressMark> = emptyList()

    override fun acceptIntent(intent: GoalStatisticsIntent) = when (intent) {
        is ArgsReceived -> onArgsReceived(intent.goalId)
        is SelectedScaleChanged -> onSelectedScaleChanged(intent.scale)
    }

    override fun mapUiState(
        previousState: GoalStatisticsUiState,
        partialState: PartialState
    ): GoalStatisticsUiState = when (partialState) {
        is BookLoaded -> previousState.copy(book = partialState.book)
        is ProgressMarksLoaded -> previousState.copy(
            visibleProgressMarks = partialState.progressMarks
        )

        is StatisticsScaleChanged -> previousState.copy(
            selectedStatisticsScale = partialState.scale,
            visibleProgressMarks = prepareProgressMarksList(
                progressMarks = goalFullProgressList,
                scale = partialState.scale
            )
        )
    }

    private fun onArgsReceived(goalId: Int) {
        if (goalId > 0) {
            loadGoalData(goalId)
        }
    }

    private fun onSelectedScaleChanged(scale: StatisticsScale) {
        if (scale != uiState.value.selectedStatisticsScale) {
            updateUiState(StatisticsScaleChanged(scale))
        }
    }

    private fun loadGoalData(goalId: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            getGoalUseCase(goalId).onSuccess {
                updateUiState(
                    BookLoaded(
                        Book(
                            title = it.bookName,
                            authorName = it.bookAuthor,
                            publishYear = it.bookPublishYear.toString()
                        )
                    )
                )

                getGoalProgressUseCase(goalId).onSuccess { progressMarks ->
                    goalFullProgressList = progressMarks

                    val visibleProgressMarks = prepareProgressMarksList(
                        progressMarks = progressMarks,
                        scale = uiState.value.selectedStatisticsScale
                    )

                    updateUiState(ProgressMarksLoaded(progressMarks = visibleProgressMarks))
                }.onError {
                    Log.e("GoalStatisticsViewModel", "Error loading goal progress, ${it.exception}")
                }
            }.onError {
                Log.e("GoalStatisticsViewModel", "Error loading goal, ${it.exception}")
            }
        }
    }

    private fun prepareProgressMarksList(
        progressMarks: List<ProgressMark>,
        scale: StatisticsScale
    ): List<ProgressMarkUiModel> {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")

        return progressMarks
            .filter {
                isDateInScaleRange(
                    date = it.date,
                    scale = scale
                )
            }.groupBy {
                LocalDateTime.parse(it.date, formatter).toLocalDate()
            }.map { dateMarksMap ->
                val dateMark = with(dateMarksMap.key) {
                    when (scale) {
                        WEEK -> dayOfWeek.value
                        MONTH -> dayOfMonth
                        YEAR -> monthValue - 1// months start from 1 so need to adjust to Y list indices
                    }
                }

                val totalValue = dateMarksMap.value.sumOf { it.pagesAmount }

                ProgressMarkUiModel(
                    dateMark = dateMark,
                    progress = totalValue
                )
            }.let { summedMarks ->
                //TODO: refactor repeated code
                if(scale == YEAR) {
                    summedMarks
                        .groupBy { it.dateMark }
                        .map { dateMarksMap ->
                            val totalValue = dateMarksMap.value.sumOf { it.progress }
                            ProgressMarkUiModel(
                                dateMark = dateMarksMap.key,
                                progress = totalValue
                            )
                        }
                }
                else summedMarks
            }.also {
                Log.e("GoalStatisticsViewModel", "summed marks: $it")
            }
    }

    private fun isDateInScaleRange(
        date: String,
        scale: StatisticsScale
    ): Boolean = when (scale) {
        WEEK -> isInWeekScale(date)
        MONTH -> isInMonthScale(date)
        YEAR -> isInYearScale(date)
    }

    private fun isInWeekScale(date: String): Boolean {
        val today = LocalDate.now()
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val sunday = monday.plusDays(6)

        val mondayMillis = monday.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        val sundayMillis = sunday.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        val progressMarkMillis = SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss",
            Locale.getDefault()
        ).parse(date)
            ?.toInstant()
            ?.toEpochMilli()
            ?: throw Exception("Failed to parse mark date. date: $date")

        /*
        Log.e("GoalStatisticsViewModel", "mondayMillis, $mondayMillis")
        Log.e("GoalStatisticsViewModel", "sundayMillis, $sundayMillis")
        Log.e("GoalStatisticsViewModel", "progressMarkMillis, $progressMarkMillis")
         */
        Log.e("GoalStatisticsViewModel", "isInWeekScale: ${progressMarkMillis in mondayMillis..sundayMillis}")


        return progressMarkMillis in mondayMillis..sundayMillis
    }

    private fun isInMonthScale(date: String): Boolean {
        val monthDays = Calendar.getInstance().getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
        val today = LocalDate.now()
        val monthFirstDay = today.with(TemporalAdjusters.firstDayOfMonth())
        val monthLastDay = monthFirstDay.plusDays((monthDays - 1).toLong())

        val monthFirstDayMillis =
            monthFirstDay.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        val monthLastDayMillis =
            monthLastDay.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        val progressMarkMillis = SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss",
            Locale.getDefault()
        ).parse(date)
            ?.toInstant()
            ?.toEpochMilli()
            ?: throw Exception("Failed to parse mark date. date: $date")

        /*
        Log.e("GoalStatisticsViewModel", "monthFirstDayMillis, $monthFirstDayMillis")
        Log.e("GoalStatisticsViewModel", "monthLastDayMillis, $monthLastDayMillis")
        Log.e("GoalStatisticsViewModel", "progressMarkMillis, $progressMarkMillis")
         */
        Log.e("GoalStatisticsViewModel", "isInMonthScale: ${progressMarkMillis in monthFirstDayMillis..monthLastDayMillis}")


        return progressMarkMillis in monthFirstDayMillis..monthLastDayMillis
    }

    private fun isInYearScale(date: String): Boolean {
        val today = LocalDate.now()
        val yearFirstDay = today.with(TemporalAdjusters.firstDayOfYear())
        val yearLastDay = yearFirstDay.plusDays(364)

        val yearFirstDayMillis =
            yearFirstDay.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        val yearLastDayMillis = yearLastDay.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        val progressMarkMillis = SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss",
            Locale.getDefault()
        ).parse(date)
            ?.toInstant()
            ?.toEpochMilli()
            ?: throw Exception("Failed to parse mark date. date: $date")

        /*
        Log.e("GoalStatisticsViewModel", "yearFirstDayMillis, $yearFirstDayMillis")
        Log.e("GoalStatisticsViewModel", "yearLastDayMillis, $yearLastDayMillis")
        Log.e("GoalStatisticsViewModel", "progressMarkMillis, $progressMarkMillis")
         */
        Log.e("GoalStatisticsViewModel", "isInYearScale: ${progressMarkMillis in yearFirstDayMillis..yearLastDayMillis}")

        return progressMarkMillis in yearFirstDayMillis..yearLastDayMillis
    }

    companion object {

        const val ARGS_GOAL_ID = "goal_id"
    }
}