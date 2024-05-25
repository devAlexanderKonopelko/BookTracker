package com.konopelko.booksgoals.domain.model.progress

/**
 * [date] - date in format "yyyy/MM/dd HH:mm:ss"
 * **/
data class ProgressMark(
    val goalId: Int,
    val date: String,
    val isBookFinished: Boolean,
    val pagesAmount: Int
)
