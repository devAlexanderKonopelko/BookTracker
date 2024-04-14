package com.konopelko.booksgoals.presentation.searchbooks.ui

import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konopelko.booksgoals.data.api.response.searchbooks.SearchBooksResponse.BookResponse
import com.konopelko.booksgoals.presentation.common.theme.BooksGoalsAppTheme
import com.konopelko.booksgoals.presentation.common.utils.debounce.DebounceEffect
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.OnSearchBooks
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.OnSearchTextChanged
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent.OnAddNewBookClicked
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksIntent.SearchBooksNavigationIntent.OnBookClicked
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksUiState
import com.konopelko.booksgoals.presentation.searchbooks.SearchBooksViewModel
import org.koin.androidx.compose.getViewModel

//todo: move onNavigate to viewModel
@Composable
fun SearchBooksScreen(
    viewModel: SearchBooksViewModel = getViewModel(),
    onNavigate: (SearchBooksNavigationIntent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    SearchBooksScreenContent(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onIntent = viewModel::acceptIntent,
        onNavigate = onNavigate
    )
}

@Composable
fun SearchBooksScreenContent(
    modifier: Modifier = Modifier,
    uiState: SearchBooksUiState,
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
                onNavigate = onNavigate
            )
            else -> SearchedBooksContent(
                modifier = Modifier.weight(1f),
                searchResults = searchResults,
                onIntent = onIntent,
                onNavigate = onNavigate
            )
        }

        DebounceEffect(input = searchText) {
            onIntent(OnSearchBooks(text = searchText))
        }
    }
}

@Composable
private fun SearchedBooksContent(
    searchResults: List<BookResponse>,
    onIntent: (SearchBooksIntent) -> Unit,
    onNavigate: (SearchBooksNavigationIntent) -> Unit,
    modifier: Modifier = Modifier
) = LazyColumn(
    modifier = modifier.fillMaxWidth()
) {
    items(searchResults) { resultItem ->
        SearchResultItem(
            item = resultItem,
            onNavigate = onNavigate
        )
    }
}

@Composable
fun SearchResultItem(
    item: BookResponse,
    onNavigate: (SearchBooksNavigationIntent) -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onNavigate(OnBookClicked(book = item))
            },
        text = item.title
    )
}

@Composable
private fun NoBooksContent(
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
            onNavigate(OnAddNewBookClicked)
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
        )
    )
}