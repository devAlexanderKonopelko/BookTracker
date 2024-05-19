package com.konopelko.booksgoals.presentation.addgoal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class AddGoalScreenOrigin: Parcelable {
    GOALS,
    ADD_WISH_BOOK,
    GOAL_DETAILS
}