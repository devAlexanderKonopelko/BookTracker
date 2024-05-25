package com.konopelko.booksgoals.presentation.wishes.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class WishesArgs(
    val isWishBookAdded: Boolean = false,
    val isSelectBookForGoal: Boolean = false
): Parcelable

class WishesArgsType : NavType<WishesArgs>(isNullableAllowed = true) {

    override fun get(bundle: Bundle, key: String): WishesArgs? = bundle.getParcelable(key)

    override fun parseValue(value: String): WishesArgs = Gson().fromJson(value, WishesArgs::class.java)

    override fun put(bundle: Bundle, key: String, value: WishesArgs) {
        bundle.putParcelable(key, value)
    }
}
