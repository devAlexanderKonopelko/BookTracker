package com.konopelko.booksgoals.presentation.goals.navigation

import androidx.navigation.NavController
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.GoalsNavigationIntent
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.GoalsNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.navigation.MainNavOption

//todo: create base impl, di
class GoalsScreenNavigation(
    private val navController: NavController
) {

    fun onNavigate(intent: GoalsNavigationIntent) = when(intent) {
        NavigateToAddGoalScreen -> navigateToAddGoalScreen()
    }

    private fun navigateToAddGoalScreen() {
        navController.navigate(MainNavOption.AddGoalScreen.name)
    }
}