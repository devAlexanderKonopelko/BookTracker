package com.konopelko.booksgoals.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.konopelko.booksgoals.presentation.achievements.ui.AchievementsScreen
import com.konopelko.booksgoals.presentation.addbook.navigation.AddBookNavigation
import com.konopelko.booksgoals.presentation.addgoal.navigation.AddGoalScreenNavigation
import com.konopelko.booksgoals.presentation.finishedbooks.ui.FinishedBooksScreen
import com.konopelko.booksgoals.presentation.goaldetails.GoalDetailsViewModel
import com.konopelko.booksgoals.presentation.goaldetails.navigation.GoalDetailsNavigation
import com.konopelko.booksgoals.presentation.goaldetails.ui.GoalDetailsScreen
import com.konopelko.booksgoals.presentation.goals.navigation.GoalsScreenNavigation
import com.konopelko.booksgoals.presentation.goals.ui.GoalsScreen
import com.konopelko.booksgoals.presentation.searchbooks.navigation.SearchBooksNavigation
import com.konopelko.booksgoals.presentation.statistics.ui.StatisticsScreen
import com.konopelko.booksgoals.presentation.wishes.WishesViewModel
import com.konopelko.booksgoals.presentation.wishes.navigation.WishesNavigation
import com.konopelko.booksgoals.presentation.wishes.ui.WishesScreen

internal val appStartScreen = MainNavOption.GoalsScreen

//todo: move composable creation to corresponding [<ScreenName>Navigation] class
fun NavGraphBuilder.mainGraph(
    navController: NavController
) {
    navigation(
        startDestination = appStartScreen.name,
        route = NavRoutes.MainRoute.name
    ) {

        AddGoalScreenNavigation(navController).navComposable(this)
        SearchBooksNavigation(navController).navComposable(this)
        AddBookNavigation(navController).navComposable(this)

        composable(MainNavOption.GoalsScreen.name) {
            val args = it.savedStateHandle.get<Boolean>("goal_added")

            GoalsScreen(
                onNavigate = GoalsScreenNavigation(navController)::onNavigate,
                args = args
            )
        }

        composable(MainNavOption.GoalDetailsScreen.name) {
            val args = it.savedStateHandle.get<Int>(GoalDetailsViewModel.ARGS_GOAL_ID_KEY)

            GoalDetailsScreen(
                onNavigate = GoalDetailsNavigation(navController)::onNavigate,
                args = args
            )
        }

        composable(MainNavOption.FinishedBooksScreen.name) {
            FinishedBooksScreen()
        }
        composable(MainNavOption.StatisticsScreen.name) {
            StatisticsScreen()
        }
        composable(MainNavOption.AchievementsScreen.name) {
            AchievementsScreen()
        }

        composable(MainNavOption.WishesScreen.name) {
            val args = it.savedStateHandle.get<Boolean>(WishesViewModel.ARGS_WISH_BOOK_ADDED_KEY)

            WishesScreen(
                onNavigate = WishesNavigation(navController)::onNavigate,
                args = args
            )
        }
    }
}

enum class MainNavOption {
    WishesScreen,
    GoalsScreen,
    FinishedBooksScreen,
    StatisticsScreen,
    AchievementsScreen,

    AddGoalScreen,
    GoalDetailsScreen,
    SearchBooksScreen,
    AddBookScreen
}