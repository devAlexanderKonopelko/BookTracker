package com.konopelko.booksgoals.domain.model.progress.mapper

import com.konopelko.booksgoals.domain.model.progress.ProgressMark
import com.konopelko.booksgoals.presentation.goalstatistics.model.ProgressMarkUiModel
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.MONTH
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.WEEK
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.YEAR

fun ProgressMark.toUiModel(scale: StatisticsScale): ProgressMarkUiModel = ProgressMarkUiModel(
    progress = pagesAmount,
//    dateMark = calculateDateMark(scale, date)
    dateMark = 1
)

private fun calculateDateMark(
    scale: StatisticsScale,
    date: String
): Int = when(scale) {
    WEEK -> calculateWeekMark(date)
    MONTH -> calculateMonthMark(date)
    YEAR -> calculateYearMark(date)
}

fun calculateYearMark(date: String): Int {
    return 1
}

fun calculateMonthMark(date: String): Int {
    return 1
}

private fun calculateWeekMark(date: String): Int {
    // 1. Вычислить, какой это день недели, и засеттить соответствующий индекс int
    return 1
}

/*todo:
   1. get millis range from selected scale
   2. calculate range step millis
   3.
* */