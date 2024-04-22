package com.konopelko.booksgoals.presentation.addbook

import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin

sealed interface AddBookIntent {

    data object OnAddBookClicked : AddBookIntent

    data class OnArgsReceived(val screenOrigin: SearchScreenOrigin) : AddBookIntent
    data class OnTitleChanged(val bookTitle: String) : AddBookIntent
    data class OnAuthorNameChanged(val authorName: String) : AddBookIntent
    data class OnPublishYearChanged(val publishYear: String) : AddBookIntent
    data class OnPagesAmountChanged(val pagesAmount: String) : AddBookIntent

    sealed interface AddBookNavigationIntent {
        data object NavigateToWishesScreen : AddBookNavigationIntent
        data class NavigateToAddGoalScreen(val book: Book) : AddBookNavigationIntent
    }
}