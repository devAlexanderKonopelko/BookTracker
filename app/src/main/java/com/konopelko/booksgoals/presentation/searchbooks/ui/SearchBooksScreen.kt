package com.konopelko.booksgoals.presentation.searchbooks.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.konopelko.booksgoals.R
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchScreenOrigin
import com.konopelko.booksgoals.presentation.common.theme.BookTrackerTheme
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
import com.konopelko.booksgoals.presentation.searchbooks.model.SearchBooksArgs
import org.koin.androidx.compose.getViewModel

//todo: move onNavigate to viewModel
@Composable
fun SearchBooksScreen(
    viewModel: SearchBooksViewModel = getViewModel(),
    onNavigate: (SearchBooksNavigationIntent) -> Unit,
    args: SearchBooksArgs
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
        viewModel.acceptIntent(OnArgsReceived(args))
    }

    if(uiState.shouldNavigateToAddGoalScreen) {
        LaunchedEffect("navigate_to_add_goal_key") {
            onNavigate(NavigateToAddGoalScreen(uiState.bookToCreateGoal))
        }
    }

    if(uiState.shouldNavigateToWishesScreen) {
        LaunchedEffect("navigate_to_wishes_key") {
            Toast.makeText(context, "Книга успешно добавлена в список желаний!", Toast.LENGTH_SHORT).show()
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
            placeholder = { Text(text = "Введите название книги") }
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SearchResultItem(
    item: Book,
    onIntent: (SearchBooksIntent) -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(
            horizontal = 16.dp,
            vertical = 8.dp
        )
        .background(
            color = Color(0xFFE7E7E7),
            shape = RoundedCornerShape(4.dp)
        )
        .clickable { onIntent(OnBookClicked(book = item)) },
) {
    Row(
        modifier = Modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(item.coverUrl.isNotEmpty()){
            GlideImage(
                modifier = Modifier.size(60.dp),
                model = item.coverUrl,
                contentDescription = "Book image",
                loading = placeholder(R.drawable.ic_default_book),
                failure = placeholder(R.drawable.ic_default_book)
            )
        } else {
            Image(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.ic_default_book),
                contentDescription = ""
            )
        }

        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "${item.authorName} - ${item.publishYear}",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
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
        text = "Ничего не найдено?",
        textAlign = TextAlign.Center
    )
    Button(
        modifier = Modifier.padding(start = 16.dp),
        onClick = {
            onNavigate(OnAddNewBookClicked(screenOrigin))
        }
    ) {
        Text(text = "Добавить новую книгу")
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
private fun SearchBooksScreenPreview() = BookTrackerTheme {
    SearchBooksScreenContent(
        uiState = SearchBooksUiState(
            searchResults = listOf(
                Book(title = "Book1", authorName = "B.C. Author", publishYear = "2001"),
                Book(title = "Book2", authorName = "B.C. Author6", publishYear = "2005"),
                Book(title = "Book3", authorName = "B.C. Author3", publishYear = "2006"),
                Book(title = "Book4", authorName = "B.C. Author4", publishYear = "2003"),
                Book(title = "Book5", authorName = "B.C. Author5", publishYear = "2002"),
            ),
            isSearching = false
        ),
        screenOrigin = SearchScreenOrigin.ADD_GOAL
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchBooksScreenEmptyPreview() = BookTrackerTheme {
    SearchBooksScreenContent(
        uiState = SearchBooksUiState(
            searchResults = listOf(),
            isSearching = false
        ),
        screenOrigin = SearchScreenOrigin.ADD_GOAL
    )
}