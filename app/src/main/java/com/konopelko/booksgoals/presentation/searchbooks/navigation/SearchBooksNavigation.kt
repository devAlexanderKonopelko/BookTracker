package com.konopelko.booksgoals.presentation.searchbooks.navigation

import android.util.Log
import androidx.navigation.NavController
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.model.booksearch.SearchScreenOrigin
import com.konopelko.booksgoals.presentation.addbook.AddBookViewModel
import com.konopelko.booksgoals.presentation.addgoal.AddGoalViewModel
import com.konopelko.booksgoals.presentation.navigation.MainNavOption
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent.NavigateToWishesScreen
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent.OnAddNewBookClicked
import com.konopelko.booksgoals.presentation.wishes.WishesViewModel

//todo: create [BaseNavigationHandler]
class SearchBooksNavigation(
    private val navController: NavController
) {

    fun onNavigate(intent: SearchBooksNavigationIntent) = when(intent) {
        NavigateToWishesScreen -> navigateToWishesScreen()
        is NavigateToAddGoalScreen -> navigateToAddGoalScreen(intent.book)
        is OnAddNewBookClicked -> navigateToAddBookScreen(intent.origin)
    }

    private fun navigateToAddBookScreen(origin: SearchScreenOrigin) {
        with(navController) {
            navigate(prepareAddBookScreenNameWithArgs(origin)) {
                currentBackStackEntry?.apply {
                    Log.e("WishesNavigation", "set search screen origin")
                    savedStateHandle[AddBookViewModel.ARGS_SCREEN_ORIGIN_KEY] = origin
                }
            }
        }
    }

    private fun navigateToWishesScreen() {
        navController.previousBackStackEntry?.savedStateHandle?.apply {
            set(WishesViewModel.ARGS_WISH_BOOK_ADDED_KEY, true)
        }

        navController.popBackStack()
    }

    private fun navigateToAddGoalScreen(book: Book) {
        navController.previousBackStackEntry?.savedStateHandle?.apply {
            set(AddGoalViewModel.ARGS_BOOK_KEY, book)
        }
        navController.navigate(MainNavOption.AddGoalScreen.name) {

            launchSingleTop = true
            popUpTo(MainNavOption.SearchBooksScreen.name) {
                inclusive = true
            }
        }
    }

    //todo: move to AddBookScreenNavigation
    private fun prepareAddBookScreenNameWithArgs(origin: SearchScreenOrigin): String =
        "${MainNavOption.AddBookScreen.name}/{${origin}}"
}