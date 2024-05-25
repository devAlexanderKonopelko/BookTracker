package com.konopelko.booksgoals.presentation.addgoal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent.NavigateToGoalDetailsScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent.NavigateToGoalsScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent.NavigateToSearchBooksScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalIntent.AddGoalNavigationIntent.NavigateToWishesScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalViewModel
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalArgs
import com.konopelko.booksgoals.presentation.addgoal.ui.AddGoalScreen
import com.konopelko.booksgoals.presentation.common.base.navigation.BaseScreenNavigation
import com.konopelko.booksgoals.presentation.navigation.MainNavOption
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalArgsType
import com.konopelko.booksgoals.presentation.wishes.WishesViewModel
import com.konopelko.booksgoals.presentation.wishes.navigation.WishesScreenNavigation

class AddGoalScreenNavigation(
    private val navController: NavController,
): BaseScreenNavigation<AddGoalNavigationIntent, AddGoalArgs>(
    screenName = MainNavOption.AddGoalScreen.name,
    defaultOptionalArgs = AddGoalArgs(),
    optionalArgsKey = AddGoalViewModel.ARGS_ADD_GOAL_KEY,
    optionalArgsType = AddGoalArgsType()
) {

    @Composable
    override fun ScreenComposable(
        onNavigate: (AddGoalNavigationIntent) -> Unit,
        args: AddGoalArgs
    ) = AddGoalScreen(
        onNavigate = onNavigate,
        args = args
    )

    override fun onNavigate(intent: AddGoalNavigationIntent) = when(intent) {
        NavigateToSearchBooksScreen -> navigateToSearchBooksScreen()
        NavigateToWishesScreen -> navigateToWishesScreen()
        NavigateToGoalsScreen -> navigateToGoalsScreen()
        NavigateToGoalDetailsScreen -> navigateToGoalDetailsScreen()
    }

    private fun navigateToGoalDetailsScreen() {
        navController.popBackStack()
    }

    //todo: pass [SearchScreenOrigin.ADD_GOAL]
    private fun navigateToSearchBooksScreen() {
        navController.navigate(MainNavOption.SearchBooksScreen.name) {
            launchSingleTop = true
        }
    }

    private fun navigateToWishesScreen() {
        navController.navigate(
            WishesScreenNavigation.prepareWishesScreenNameWithArgs(
                isBookAdded = false,
                isSelectBookForGoal = true
            )
        )
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
