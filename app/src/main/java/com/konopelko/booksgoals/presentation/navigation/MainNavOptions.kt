package com.konopelko.booksgoals.presentation.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse.BookResponse
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.model.booksearch.SearchScreenOrigin
import com.konopelko.booksgoals.domain.model.booksearch.SearchScreenOrigin.ADD_GOAL
import com.konopelko.booksgoals.presentation.achievements.ui.AchievementsScreen
import com.konopelko.booksgoals.presentation.addbook.navigation.AddBookNavigation
import com.konopelko.booksgoals.presentation.addbook.ui.AddBookScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalViewModel
import com.konopelko.booksgoals.presentation.addgoal.navigation.AddGoalScreenNavigation
import com.konopelko.booksgoals.presentation.addgoal.ui.AddGoalScreen
import com.konopelko.booksgoals.presentation.finishedbooks.ui.FinishedBooksScreen
import com.konopelko.booksgoals.presentation.goals.navigation.GoalsScreenNavigation
import com.konopelko.booksgoals.presentation.goals.ui.GoalsScreen
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksViewModel
import com.konopelko.booksgoals.presentation.searchbooks.navigation.SearchBooksNavigation
import com.konopelko.booksgoals.presentation.searchbooks.ui.SearchBooksScreen
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
        composable(MainNavOption.AddGoalScreen.name) {
            val args = it.savedStateHandle.get<Book>(AddGoalViewModel.ARGS_BOOK_KEY)

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
            val args = it.savedStateHandle.get<Boolean>(WishesViewModel.ARGS_WISH_BOOK_ADDED_KEY)

            WishesScreen(
                onNavigate = WishesNavigation(navController)::onNavigate,
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
        //todo: move preparing screen name with args to [SearchBooksNavigation]
        composable(MainNavOption.SearchBooksScreen.name+"/{screen_origin}") { backStackEntry ->
            // todo: refactor somehow args receiving
            val args = backStackEntry
                .arguments
                ?.getString(SearchBooksViewModel.ARGS_SCREEN_ORIGIN_KEY)
                ?.let { screenOriginName ->
                    val preparedOriginScreenName = screenOriginName.filter { it != '{' && it != '}' }
                    SearchScreenOrigin.valueOf(preparedOriginScreenName)
                }

            Log.e("Navigation", "search books screen navigated, args: $args")

            SearchBooksScreen(
                onNavigate = SearchBooksNavigation(navController)::onNavigate,
                args = args
            )
        }
        composable(MainNavOption.AddBookScreen.name+"/{screen_origin}") { backStackEntry ->
            // todo: refactor somehow args receiving
            val args = backStackEntry
                .arguments
                ?.getString(SearchBooksViewModel.ARGS_SCREEN_ORIGIN_KEY)
                ?.let { screenOriginName ->
                    val preparedOriginScreenName = screenOriginName.filter { it != '{' && it != '}' }
                    SearchScreenOrigin.valueOf(preparedOriginScreenName)
                }

            Log.e("Navigation", "add book screen navigated, args: $args")

            AddBookScreen(
                onNavigate = AddBookNavigation(navController)::onNavigate,
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
    SearchBooksScreen,
    AddBookScreen
}