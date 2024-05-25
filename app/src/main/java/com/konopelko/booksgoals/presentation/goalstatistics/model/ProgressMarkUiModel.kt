package com.konopelko.booksgoals.presentation.goalstatistics.model

/**
 * [progress] - pages or books read per [dateMark]
 * [dateMark] - order number of day/month depending on [StatisticsScale]
 */
data class ProgressMarkUiModel(
    val progress: Int,
    val dateMark: Int
)
