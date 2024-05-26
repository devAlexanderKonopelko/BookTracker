package com.konopelko.booksgoals.presentation.searchbooks.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin
import com.konopelko.booksgoals.presentation.addgoal.AddGoalViewModel
import com.konopelko.booksgoals.presentation.addgoal.model.AddGoalArgs
import com.konopelko.booksgoals.presentation.common.base.navigation.BaseScreenNavigation
import com.konopelko.booksgoals.presentation.navigation.MainNavOption
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent.NavigateToWishesScreen
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent.OnAddNewBookClicked
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksViewModel
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchBooksArgs
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchBooksArgsType
import com.konopelko.booksgoals.presentation.searchbooks.ui.SearchBooksScreen
import com.konopelko.booksgoals.presentation.wishes.WishesViewModel
import com.konopelko.booksgoals.presentation.wishes.model.WishesArgs
import com.konopelko.booksgoals.presentation.wishes.navigation.WishesScreenNavigation

class SearchBooksNavigation(
    private val navController: NavController
): BaseScreenNavigation<SearchBooksNavigationIntent, SearchBooksArgs>(
    screenName = MainNavOption.SearchBooksScreen.name,
    defaultOptionalArgs = SearchBooksArgs(),
    optionalArgsKey = SearchBooksViewModel.ARGS_SCREEN_ORIGIN_KEY,
    optionalArgsType = SearchBooksArgsType()
) {

    @Composable
    override fun ScreenComposable(
        onNavigate: (SearchBooksNavigationIntent) -> Unit,
        args: SearchBooksArgs
    ) = SearchBooksScreen(
        onNavigate = onNavigate,
        args = args
    )

    override fun onNavigate(intent: SearchBooksNavigationIntent) = when(intent) {
        NavigateToWishesScreen -> navigateToWishesScreen()
        is NavigateToAddGoalScreen -> navigateToAddGoalScreen(intent.book)
        is OnAddNewBookClicked -> navigateToAddBookScreen(intent.origin)
    }

    private fun navigateToAddBookScreen(origin: SearchScreenOrigin) {
        navController.navigate(prepareAddBookScreenNameWithArgs(origin))
    }

    private fun navigateToWishesScreen() {
        navController.previousBackStackEntry?.savedStateHandle?.let {
            it[WishesViewModel.ARGS_KEY] = WishesArgs(
                isWishBookAdded = true,
                isSelectBookForGoal = false
            )
        }

        navController.popBackStack()
    }

    private fun navigateToAddGoalScreen(book: Book) {
        navController.previousBackStackEntry?.savedStateHandle?.apply {
            set(AddGoalViewModel.ARGS_ADD_GOAL_KEY, AddGoalArgs(selectedBook = book))
        }
        navController.navigate(MainNavOption.AddGoalScreen.name) {

            launchSingleTop = true
            popUpTo(MainNavOption.SearchBooksScreen.name) {
                inclusive = true
            }
        }
    }

    private fun prepareAddBookScreenNameWithArgs(origin: SearchScreenOrigin): String =
        "${MainNavOption.AddBookScreen.name}?screen_origin=$origin"
}