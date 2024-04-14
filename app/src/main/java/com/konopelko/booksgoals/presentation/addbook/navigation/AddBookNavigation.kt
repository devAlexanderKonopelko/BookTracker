package com.konopelko.booksgoals.presentation.addbook.navigation

import androidx.navigation.NavController
import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse.BookResponse
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.AddBookNavigationIntent
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.AddBookNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.navigation.MainNavOption

class AddBookNavigation(
    private val navController: NavController
) {

    fun onNavigate(intent: AddBookNavigationIntent) = when(intent) {
        is NavigateToAddGoalScreen -> navigateToAddGoalScreen(intent.book)
    }

    private fun navigateToAddGoalScreen(book: Book) {
        navController.apply {
            navigate(MainNavOption.AddGoalScreen.name) {
                popUpTo(MainNavOption.GoalsScreen.name)
            }

            currentBackStackEntry?.apply {
                //todo: replace with [Book] domain model
                //todo: make key constant
                savedStateHandle["book"] = BookResponse(
                    title = book.title,
                    authorName = listOf(book.authorName),
                    publishYear = book.publishYear.toInt(),
                    pagesAmount = book.pagesAmount.toInt()
                )
            }
        }
    }
}