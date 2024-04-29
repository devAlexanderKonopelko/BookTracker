package com.konopelko.booksgoals.presentation.goals.navigation

import androidx.navigation.NavController
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsViewModel
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.GoalsNavigationIntent
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.GoalsNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.GoalsNavigationIntent.NavigateToGoalDetailsScreen
import com.konopelko.booksgoals.presentation.navigation.MainNavOption
import com.konopelko.booksgoals.presentation.wishes.WishesViewModel

//todo: add base impl, di
class GoalsScreenNavigation(
    private val navController: NavController
) {

    fun onNavigate(intent: GoalsNavigationIntent) = when(intent) {
        NavigateToAddGoalScreen -> navigateToAddGoalScreen()
        is NavigateToGoalDetailsScreen -> navigateToGoalDetailsScreen(intent.goalId)
    }

    private fun navigateToAddGoalScreen() {
        navController.navigate(MainNavOption.AddGoalScreen.name)
    }

    private fun navigateToGoalDetailsScreen(goalId: Int) {
        navController.navigate(MainNavOption.GoalDetailsScreen.name)

        navController.currentBackStackEntry?.apply {
            savedStateHandle[GoalDetailsViewModel.ARGS_GOAL_ID_KEY] = goalId
        }
    }
}