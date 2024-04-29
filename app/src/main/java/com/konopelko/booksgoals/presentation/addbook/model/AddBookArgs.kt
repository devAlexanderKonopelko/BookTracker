package com.konopelko.booksgoals.presentation.addbook.model

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalArgs
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin

class AddBookArgsType : NavType<SearchScreenOrigin>(isNullableAllowed = true) {

    override fun get(bundle: Bundle, key: String): SearchScreenOrigin? = bundle.getParcelable(key)

    override fun parseValue(value: String): SearchScreenOrigin =
        Gson().fromJson(value, SearchScreenOrigin::class.java)

    override fun put(bundle: Bundle, key: String, value: SearchScreenOrigin) {
        bundle.putParcelable(key, value)
    }
}