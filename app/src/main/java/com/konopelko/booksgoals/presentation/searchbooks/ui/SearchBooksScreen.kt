package com.konopelko.booksgoals.presentation.searchbooks.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.domain.model.booksearch.SearchScreenOrigin
import com.konopelko.booksgoals.presentation.common.theme.BooksGoalsAppTheme
import com.konopelko.booksgoals.presentation.common.utils.debounce.DebounceEffect
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.OnBookClicked
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.OnSearchBooks
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.OnSearchTextChanged
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent.NavigateToWishesScreen
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent.OnAddNewBookClicked
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksUiState
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksViewModel
import org.koin.androidx.compose.getViewModel

//todo: move onNavigate to viewModel
@Composable
fun SearchBooksScreen(
    viewModel: SearchBooksViewModel = getViewModel(),
    onNavigate: (SearchBooksNavigationIntent) -> Unit,
    args: SearchScreenOrigin?
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    SearchBooksScreenContent(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onIntent = viewModel::acceptIntent,
        screenOrigin = viewModel.screenOrigin,
        onNavigate = onNavigate
    )

    LaunchedEffect("args_key") {
        args?.let { viewModel.acceptIntent(OnArgsReceived(it)) }
    }

    if(uiState.shouldNavigateToAddGoalScreen) {
        LaunchedEffect("navigate_to_add_goal_key") {
            onNavigate(NavigateToAddGoalScreen(uiState.bookToCreateGoal))
        }
    }

    if(uiState.shouldNavigateToWishesScreen) {
        LaunchedEffect("navigate_to_wishes_key") {
            Toast.makeText(context, "Book added to wishes successfully!", Toast.LENGTH_SHORT).show()
            onNavigate(NavigateToWishesScreen)
        }
    }
}

@Composable
fun SearchBooksScreenContent(
    modifier: Modifier = Modifier,
    uiState: SearchBooksUiState,
    screenOrigin: SearchScreenOrigin,
    onIntent: (SearchBooksIntent) -> Unit = {},
    onNavigate: (SearchBooksNavigationIntent) -> Unit = {}
) = Column(
    modifier = modifier
) {
    Log.e("SearchBooksScreen", "text: ${uiState.searchText}")

    with(uiState) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = searchText,
            onValueChange = {
                onIntent(OnSearchTextChanged(text = it))
            },
            placeholder = { Text(text = "Type book name") }
        )

        when {
            isSearching -> LoadingContent()
            searchResults.isEmpty() -> NoBooksContent(
                screenOrigin = screenOrigin,
                onNavigate = onNavigate
            )
            else -> SearchedBooksContent(
                modifier = Modifier.weight(1f),
                searchResults = searchResults,
                onIntent = onIntent
            )
        }

        DebounceEffect(input = searchText) {
            onIntent(OnSearchBooks(text = searchText))
        }
    }
}

@Composable
private fun SearchedBooksContent(
    searchResults: List<Book>,
    onIntent: (SearchBooksIntent) -> Unit,
    modifier: Modifier = Modifier
) = LazyColumn(
    modifier = modifier.fillMaxWidth()
) {
    items(searchResults) { resultItem ->
        SearchResultItem(
            item = resultItem,
            onIntent = onIntent
        )
    }
}

@Composable
fun SearchResultItem(
    item: Book,
    onIntent: (SearchBooksIntent) -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onIntent(OnBookClicked(book = item))
            },
        text = item.title
    )
}

@Composable
private fun NoBooksContent(
    screenOrigin: SearchScreenOrigin,
    onNavigate: (SearchBooksNavigationIntent) -> Unit
) = Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    Text(
        text = "No books are found yet",
        textAlign = TextAlign.Center
    )
    Button(
        modifier = Modifier.padding(start = 16.dp),
        onClick = {
            onNavigate(OnAddNewBookClicked(screenOrigin))
        }
    ) {
        Text(text = "Add new book")
    }
}

@Composable
private fun LoadingContent() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    CircularProgressIndicator(modifier = Modifier.size(50.dp))
}

@Preview(showBackground = true)
@Composable
private fun SearchBooksScreenPreview() = BooksGoalsAppTheme {
    SearchBooksScreenContent(
        uiState = SearchBooksUiState(
            searchResults = listOf(),
            isSearching = false
        ),
        screenOrigin = SearchScreenOrigin.ADD_GOAL
    )
}