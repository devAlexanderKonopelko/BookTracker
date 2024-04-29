package com.konopelko.booksgoals.presentation.searchbooks.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin.ADD_GOAL
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchBooksArgs(
    val screenOrigin: SearchScreenOrigin = ADD_GOAL
): Parcelable

//todo: pass [SearchScreenOrigin] directly, remove [SearchBooksArgs]
class SearchBooksArgsType : NavType<SearchBooksArgs>(isNullableAllowed = true) {

    override fun get(bundle: Bundle, key: String): SearchBooksArgs? = bundle.getParcelable(key)

    override fun parseValue(value: String): SearchBooksArgs = Gson().fromJson(value, SearchBooksArgs::class.java)

    override fun put(bundle: Bundle, key: String, value: SearchBooksArgs) {
        bundle.putParcelable(key, value)
    }
}