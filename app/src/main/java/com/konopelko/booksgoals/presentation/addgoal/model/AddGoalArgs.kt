package com.konopelko.booksgoals.presentation.addgoal.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import com.konopelko.booksgoals.domain.model.book.Book
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddGoalArgs(
    val screenOrigin: AddGoalScreenOrigin = AddGoalScreenOrigin.GOALS,
    val selectedBook: Book? = null
): Parcelable

class AddGoalArgsType : NavType<AddGoalArgs>(isNullableAllowed = true) {

    override fun get(bundle: Bundle, key: String): AddGoalArgs? = bundle.getParcelable(key)

    override fun parseValue(value: String): AddGoalArgs = Gson().fromJson(value, AddGoalArgs::class.java)

    override fun put(bundle: Bundle, key: String, value: AddGoalArgs) {
        bundle.putParcelable(key, value)
    }
}
