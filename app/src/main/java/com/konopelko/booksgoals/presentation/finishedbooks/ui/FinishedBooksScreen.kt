package com.konopelko.booksgoals.presentation.finishedbooks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.common.theme.BooksGoalsAppTheme
import com.konopelko.booksgoals.presentation.common.theme.Typography
import com.konopelko.booksgoals.presentation.finishedbooks.FinishedBooksViewModel
import com.konopelko.booksgoals.presentation.goals.GoalsIntent.GoalsNavigationIntent.NavigateToAddGoalScreen
import org.koin.androidx.compose.getViewModel

@Composable
fun FinishedBooksScreen(
    viewModel: FinishedBooksViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.books.isNotEmpty() -> FinishedBooksContent(finishedBooks = uiState.books)
        else -> NoFinishedBooksContent()
    }
}

@Composable
private fun FinishedBooksContent(
    finishedBooks: List<Book>
) = Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    FinishedBooksHeader()

    FinishedBooksListContent(finishedBooks)
}

@Composable
private fun FinishedBooksHeader() = Text(
    modifier = Modifier.padding(top = 16.dp),
    text = "My finished books",
    style = Typography.headlineLarge,
)

@Composable
private fun FinishedBooksListContent(
    finishedBooks: List<Book>
) {
    LazyColumn(
        modifier = Modifier
            .padding(
                top = 24.dp,
                start = 16.dp,
                end = 16.dp
            )
    ) {
        items(finishedBooks) { book ->
            FinishedBookCard(book)
        }
    }
}

@Composable
private fun FinishedBookCard(
    book: Book
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
        .background(
            color = Color.LightGray,
            shape = RoundedCornerShape(12.dp)
        )
        .border(3.dp, Color.Green, RoundedCornerShape(12.dp)),
    verticalAlignment = Alignment.CenterVertically
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .padding(
                top = 4.dp,
                bottom = 4.dp,
                start = 8.dp
            )
            .background(
                color = Color.Gray,
                shape = RoundedCornerShape(4.dp)
            )
    )

    Column(
        modifier = Modifier.padding(start = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            modifier = Modifier.fillMaxWidth(0.8f),
            text = book.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row {
            Text(text = book.authorName)

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = book.publishYear
            )
        }
    }
}

@Composable
private fun NoFinishedBooksContent() = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = "No finished books yet",
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
private fun FinishedBooksPreview() = BooksGoalsAppTheme {
    FinishedBooksContent(
        finishedBooks = listOf(
            Book(
                title = "Book A",
                authorName = "Author A",
                publishYear = "2007"
            ),
            Book(
                title = "Book B",
                authorName = "Author B",
                publishYear = "2008"
            ),
            Book(
                title = "Book C",
                authorName = "Author C",
                publishYear = "2009"
            ),
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun NoFinishedBooksPreview() = BooksGoalsAppTheme {
    NoFinishedBooksContent()
}