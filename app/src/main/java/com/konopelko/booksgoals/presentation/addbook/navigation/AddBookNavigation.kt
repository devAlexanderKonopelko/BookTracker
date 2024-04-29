package com.konopelko.booksgoals.presentation.addbook.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.AddBookNavigationIntent
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.AddBookNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.AddBookNavigationIntent.NavigateToWishesScreen
import com.konopelko.booksgoals.presentation.addbook.AddBookViewModel
import com.konopelko.booksgoals.presentation.addbook.model.AddBookArgsType
import com.konopelko.booksgoals.presentation.addbook.ui.AddBookScreen
import com.konopelko.booksgoals.presentation.addgoal.AddGoalViewModel
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalArgs
import com.konopelko.booksgoals.presentation.common.base.navigation.BaseScreenNavigation
import com.konopelko.booksgoals.presentation.navigation.MainNavOption
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin.ADD_GOAL
import com.konopelko.booksgoals.presentation.wishes.WishesViewModel

class AddBookNavigation(
    private val navController: NavController
) : BaseScreenNavigation<AddBookNavigationIntent, SearchScreenOrigin>(
    screenName = MainNavOption.AddBookScreen.name,
    defaultOptionalArgs = ADD_GOAL,
    optionalArgsKey = AddBookViewModel.ARGS_SCREEN_ORIGIN_KEY,
    optionalArgsType = AddBookArgsType()
) {

    @Composable
    override fun ScreenComposable(
        onNavigate: (AddBookNavigationIntent) -> Unit,
        args: SearchScreenOrigin
    ) = AddBookScreen(
        onNavigate = onNavigate,
        args = args
    )

    override fun onNavigate(intent: AddBookNavigationIntent) = when(intent) {
        NavigateToWishesScreen -> navigateToWishesScreen()
        is NavigateToAddGoalScreen -> navigateToAddGoalScreen(intent.book)
    }

    private fun navigateToWishesScreen() {
        navController.apply {
            navigate(MainNavOption.WishesScreen.name) {
                popUpTo(navController.graph.startDestinationId)
            }

            currentBackStackEntry?.apply {
                savedStateHandle[WishesViewModel.ARGS_WISH_BOOK_ADDED_KEY] = true
            }
        }
    }

    private fun navigateToAddGoalScreen(book: Book) {
        Log.e("AddBookNavigation", "navigateToAddGoalScreen called")
        navController.apply {
            navigate(MainNavOption.AddGoalScreen.name) {
                launchSingleTop = true
                popUpTo(MainNavOption.AddGoalScreen.name)
            }

            currentBackStackEntry?.apply {
                savedStateHandle[AddGoalViewModel.ARGS_ADD_GOAL_KEY] = AddGoalArgs(selectedBook = book)
            }
        }
    }
}