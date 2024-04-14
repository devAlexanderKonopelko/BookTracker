package com.konopelko.booksgoals.presentation.addbook.navigation

import androidx.navigation.NavController
import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse.BookResponse
import com.konopelko.booksgoals.domain.model.goal.Book
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
            getBackStackEntry(MainNavOption.AddGoalScreen.name).savedStateHandle.apply {
                set(
                    "book",
                    BookResponse( //todo: replace with [Book] domain model
                        title = book.title,
                        authorName = listOf(book.authorName),
                        publishYear = book.publishYear.toInt(),
                        pagesAmount = book.pagesAmount.toInt()
                    )
                ) //todo: make key constant
            }

            navigate(MainNavOption.AddGoalScreen.name) {
                launchSingleTop = true

                popUpTo(MainNavOption.AddBookScreen.name) {
                    inclusive = true
                }
            }
        }
    }
}