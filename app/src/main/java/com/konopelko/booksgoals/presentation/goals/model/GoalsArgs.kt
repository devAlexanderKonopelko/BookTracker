package com.konopelko.booksgoals.presentation.goals.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoalsArgs(
    val isGoalAdded: Boolean = false
): Parcelable

class GoalsArgsType : NavType<GoalsArgs>(isNullableAllowed = true) {

    override fun get(bundle: Bundle, key: String): GoalsArgs? = bundle.getParcelable(key)

    override fun parseValue(value: String): GoalsArgs = Gson().fromJson(value, GoalsArgs::class.java)

    override fun put(bundle: Bundle, key: String, value: GoalsArgs) {
        bundle.putParcelable(key, value)
    }
}
