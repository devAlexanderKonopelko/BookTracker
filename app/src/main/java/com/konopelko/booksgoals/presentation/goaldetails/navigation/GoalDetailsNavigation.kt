package com.konopelko.booksgoals.presentation.goaldetails.navigation

import androidx.navigation.NavController
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsIntent.GoalDetailsNavigationIntent

class GoalDetailsNavigation(
    private val navController: NavController
) {

    fun onNavigate(intent: GoalDetailsNavigationIntent) = when(intent) {
        else -> {}
    }
}