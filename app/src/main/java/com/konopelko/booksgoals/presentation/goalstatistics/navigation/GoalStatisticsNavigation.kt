package com.konopelko.booksgoals.presentation.goalstatistics.navigation

import androidx.navigation.NavController
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsIntent.GoalStatisticsNavigationIntent

class GoalStatisticsNavigation(
    private val navController: NavController
) {

    fun onNavigate(intent: GoalStatisticsNavigationIntent) = when(intent) {
        else -> {}
    }
}