package com.konopelko.booksgoals.presentation.navigation.args

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalArgs

class AddGoalArgsType : NavType<AddGoalArgs>(isNullableAllowed = true) {

    override fun get(bundle: Bundle, key: String): AddGoalArgs? = bundle.getParcelable(key)

    override fun parseValue(value: String): AddGoalArgs = Gson().fromJson(value, AddGoalArgs::class.java)

    override fun put(bundle: Bundle, key: String, value: AddGoalArgs) {
        bundle.putParcelable(key, value)
    }
}