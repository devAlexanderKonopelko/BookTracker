package com.konopelko.booksgoals.domain.model.booksearch

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class SearchScreenOrigin : Parcelable {
    ADD_GOAL,
    ADD_WISH_BOOK
}