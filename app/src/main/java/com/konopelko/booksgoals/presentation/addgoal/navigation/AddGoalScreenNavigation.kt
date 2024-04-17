package com.konopelko.booksgoals.presentation.addgoal.navigation

import androidx.navigation.NavController
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent.NavigateToGoalsScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent.NavigateToSearchBooksScreen
import com.konopelko.booksgoals.presentation.navigation.MainNavOption

class AddGoalScreenNavigation(
    private val navController: NavController
) {

    fun onNavigate(intent: AddGoalNavigationIntent) = when(intent) {
        NavigateToSearchBooksScreen -> navigateToSearchBooksScreen()
        NavigateToGoalsScreen -> navigateToGoalsScreen()
    }

    //todo: pass [SearchScreenOrigin.ADD_GOAL]
    private fun navigateToSearchBooksScreen() {
        navController.navigate(MainNavOption.SearchBooksScreen.name) {
            launchSingleTop = true
        }
    }

    private fun navigateToGoalsScreen() {
        navController.previousBackStackEntry?.savedStateHandle?.apply {
            set("goal_added", true) //todo: make key constant
        }
        navController.navigate(MainNavOption.GoalsScreen.name) {
            popUpTo(navController.graph.startDestinationId)
        }
    }
}