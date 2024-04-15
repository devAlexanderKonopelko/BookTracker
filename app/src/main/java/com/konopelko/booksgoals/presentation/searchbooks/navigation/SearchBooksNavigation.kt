package com.konopelko.booksgoals.presentation.searchbooks.navigation

import androidx.navigation.NavController
import com.konopelko.booksgoals.domain.model.book.Book
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
        OnAddNewBookClicked -> navigateToAddBookScreen()
        NavigateToWishesScreen -> navigateToWishesScreen()
        is NavigateToAddGoalScreen -> navigateToAddGoalScreen(intent.book)
    }

    private fun navigateToAddBookScreen() {
        navController.navigate(MainNavOption.AddBookScreen.name)
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
}