package com.konopelko.booksgoals.presentation.goalstatistics

import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.goalstatistics.model.ProgressMarkUiModel
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale

data class GoalStatisticsUiState(
    val book: Book = Book(),
    val visibleProgressMarks: List<ProgressMarkUiModel> = emptyList(),
    val selectedStatisticsScale: StatisticsScale = StatisticsScale.WEEK
) {

    sealed interface PartialState {

        data class BookLoaded(val book: Book) : PartialState
        data class ProgressMarksLoaded(val progressMarks: List<ProgressMarkUiModel>) : PartialState
        data class StatisticsScaleChanged(val scale: StatisticsScale) : PartialState
    }
}