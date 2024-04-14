package com.konopelko.booksgoals.presentation.searchbooks.navigation

import androidx.navigation.NavController
import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse.BookResponse
import com.konopelko.booksgoals.presentation.navigation.MainNavOption
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent.OnAddNewBookClicked
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent.OnBookClicked

class SearchBooksNavigation(
    private val navController: NavController
) {

    fun onNavigate(intent: SearchBooksNavigationIntent) = when(intent) {
        OnAddNewBookClicked -> navigateToAddBookScreen()
        is OnBookClicked -> navigateToAddGoalScreen(intent.book)
    }

    private fun navigateToAddGoalScreen(book: BookResponse) {
        navController.previousBackStackEntry?.savedStateHandle?.apply {
            set("book", book) //todo: make key constant
        }
        navController.navigate(MainNavOption.AddGoalScreen.name) {

            launchSingleTop = true
            popUpTo(MainNavOption.SearchBooksScreen.name) {
                inclusive = true
            }
        }
    }

    private fun navigateToAddBookScreen() {
        navController.navigate(MainNavOption.AddBookScreen.name)
    }
}