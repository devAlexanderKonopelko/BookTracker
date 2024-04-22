package com.konopelko.booksgoals.presentation.addgoal.model

import android.os.Parcelable
import com.konopelko.booksgoals.domain.model.book.Book
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddGoalArgs(
    val screenOrigin: AddGoalScreenOrigin = AddGoalScreenOrigin.GOALS,
    val selectedBook: Book? = null
): Parcelable
