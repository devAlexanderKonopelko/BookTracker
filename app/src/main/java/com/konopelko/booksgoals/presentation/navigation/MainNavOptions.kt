package com.konopelko.booksgoals.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse.BookResponse
import com.konopelko.booksgoals.presentation.achievements.ui.AchievementsScreen
import com.konopelko.booksgoals.presentation.addbook.navigation.AddBookNavigation
import com.konopelko.booksgoals.presentation.addbook.ui.AddBookScreen
import com.konopelko.booksgoals.presentation.addgoal.navigation.AddGoalScreenNavigation
import com.konopelko.booksgoals.presentation.addgoal.ui.AddGoalScreen
import com.konopelko.booksgoals.presentation.books.ui.BooksScreen
import com.konopelko.booksgoals.presentation.goals.navigation.GoalsScreenNavigation
import com.konopelko.booksgoals.presentation.goals.ui.GoalsScreen
import com.konopelko.booksgoals.presentation.searchbooks.navigation.SearchBooksNavigation
import com.konopelko.booksgoals.presentation.searchbooks.ui.SearchBooksScreen
import com.konopelko.booksgoals.presentation.statistics.ui.StatisticsScreen
import com.konopelko.booksgoals.presentation.wishes.ui.WishesScreen

internal val appStartScreen = MainNavOption.GoalsScreen

fun NavGraphBuilder.mainGraph(
    navController: NavController
) {
    navigation(
        startDestination = appStartScreen.name,
        route = NavRoutes.MainRoute.name
    ) {
        composable(MainNavOption.AddGoalScreen.name) {
            val args = it.savedStateHandle.get<BookResponse>("book") //todo: make key constant

            AddGoalScreen(
                onNavigate = AddGoalScreenNavigation(navController)::onNavigate,
                args = args
            )
        }
        composable(MainNavOption.GoalsScreen.name) {
            val args = it.savedStateHandle.get<Boolean>("goal_added") //todo: make key constant

            GoalsScreen(
                onNavigate = GoalsScreenNavigation(navController)::onNavigate,
                args = args
            )
        }
        composable(MainNavOption.WishesScreen.name) {
            WishesScreen()
        }
        composable(MainNavOption.BooksScreen.name) {
            BooksScreen()
        }
        composable(MainNavOption.StatisticsScreen.name) {
            StatisticsScreen()
        }
        composable(MainNavOption.AchievementsScreen.name) {
            AchievementsScreen()
        }
        composable(MainNavOption.SearchBooksScreen.name) {
            SearchBooksScreen(onNavigate = SearchBooksNavigation(navController)::onNavigate)
        }
        composable(MainNavOption.AddBookScreen.name) {
            AddBookScreen(onNavigate = AddBookNavigation(navController)::onNavigate)
        }
    }
}

enum class MainNavOption {
    WishesScreen,
    GoalsScreen,
    BooksScreen,
    StatisticsScreen,
    AchievementsScreen,

    AddGoalScreen,
    SearchBooksScreen,
    AddBookScreen
}