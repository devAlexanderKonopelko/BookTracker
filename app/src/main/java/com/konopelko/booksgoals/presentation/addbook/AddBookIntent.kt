package com.konopelko.booksgoals.presentation.addbook

import com.konopelko.booksgoals.domain.model.goal.Book

sealed interface AddBookIntent {

    data object OnAddBookClicked : AddBookIntent

    data class OnTitleChanged(val bookTitle: String) : AddBookIntent
    data class OnAuthorNameChanged(val authorName: String) : AddBookIntent
    data class OnPublishYearChanged(val publishYear: String) : AddBookIntent
    data class OnPagesAmountChanged(val pagesAmount: String) : AddBookIntent

    sealed interface AddBookNavigationIntent {
        data class NavigateToAddGoalScreen(val book: Book) : AddBookNavigationIntent
    }
}