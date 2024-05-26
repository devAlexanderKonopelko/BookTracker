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
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsScreen
import com.konopelko.booksgoals.presentation.goalstatistics.GoalStatisticsViewModel
import com.konopelko.booksgoals.presentation.goalstatistics.navigation.GoalStatisticsNavigation
import com.konopelko.booksgoals.presentation.info.ui.InfoScreen
import com.konopelko.booksgoals.presentation.searchbooks.navigation.SearchBooksNavigation
import com.konopelko.booksgoals.presentation.totalstatistics.navigation.TotalStatisticsNavigation
import com.konopelko.booksgoals.presentation.totalstatistics.ui.TotalStatisticsScreen
import com.konopelko.booksgoals.presentation.wishes.WishesViewModel
import com.konopelko.booksgoals.presentation.wishes.model.WishesArgs
import com.konopelko.booksgoals.presentation.wishes.navigation.WishesScreenNavigation
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
        WishesScreenNavigation(navController).navComposable(this)

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
        composable(MainNavOption.TotalStatisticsScreen.name) {
            TotalStatisticsScreen(
                onNavigate = TotalStatisticsNavigation(navController)::onNavigate
            )
        }
        composable(MainNavOption.AchievementsScreen.name) {
            AchievementsScreen()
        }

        composable(MainNavOption.GoalStatisticsScreen.name) {
            val args = it.savedStateHandle.get<Int>(GoalStatisticsViewModel.ARGS_GOAL_ID)

            GoalStatisticsScreen(
                onNavigate = GoalStatisticsNavigation(navController)::onNavigate,
                args = args
            )
        }

        composable(MainNavOption.InfoScreen.name) {
            InfoScreen()
        }
    }
}

enum class MainNavOption {
    WishesScreen,
    GoalsScreen,
    FinishedBooksScreen,
    TotalStatisticsScreen,
    AchievementsScreen, // temporary disabled for future feature
    InfoScreen,

    AddGoalScreen,
    GoalDetailsScreen,
    GoalStatisticsScreen,
    SearchBooksScreen,
    AddBookScreen
}