package com.konopelko.booksgoals.presentation.searchbooks.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class SearchScreenOrigin : Parcelable {
    ADD_GOAL,
    ADD_WISH_BOOK
}