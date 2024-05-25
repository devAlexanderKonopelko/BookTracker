package com.konopelko.booksgoals.domain.utils.progressmaks

import android.util.Log
import com.konopelko.booksgoals.domain.model.progress.ProgressMark
import com.konopelko.booksgoals.presentation.common.utils.date.isDateInScaleRange
import com.konopelko.booksgoals.presentation.goalstatistics.model.ProgressMarkUiModel
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.MONTH
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.WEEK
import com.konopelko.booksgoals.presentation.goalstatistics.model.StatisticsScale.YEAR
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")

fun List<ProgressMark>.prepareProgressMarksInScale(
    scale: StatisticsScale,
    isBooksFinishedMarks: Boolean = false
): List<ProgressMarkUiModel> =
    filter {
        it.date.isDateInScaleRange(scale)
    }.groupBy {
        LocalDateTime.parse(it.date, dateFormatter).toLocalDate()
    }.map { dateMarksMap ->
        val dateMark = with(dateMarksMap.key) {
            when (scale) {
                WEEK -> dayOfWeek.value
                MONTH -> dayOfMonth
                YEAR -> monthValue - 1// months start from 1 so need to adjust to Y list indices
            }
        }

        val totalValue = dateMarksMap.value.sumOf {
            if (isBooksFinishedMarks) {
                if (it.isBookFinished) 1
                else 0
            } else it.pagesAmount
        }

        ProgressMarkUiModel(
            dateMark = dateMark,
            progress = totalValue
        )
    }.let { summedMarks ->
        //TODO: refactor repeated code
        if (scale == YEAR) {
            summedMarks
                .groupBy { it.dateMark }
                .map { dateMarksMap ->
                    val totalValue = dateMarksMap.value.sumOf { it.progress }

                    ProgressMarkUiModel(
                        dateMark = dateMarksMap.key,
                        progress = totalValue
                    )
                }
        } else summedMarks
    }.also {
        Log.e("ProgressMarksUtils", "summed marks: $it")
    }
