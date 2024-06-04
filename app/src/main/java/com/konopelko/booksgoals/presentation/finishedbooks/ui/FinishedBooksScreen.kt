package com.konopelko.booksgoals.presentation.finishedbooks.ui

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.konopelko.booksgoals.presentation.common.theme.BookTrackerTheme
import com.konopelko.booksgoals.presentation.common.theme.Typography
import com.konopelko.booksgoals.presentation.common.utils.border.drawRightBorder
import com.konopelko.booksgoals.presentation.finishedbooks.FinishedBooksViewModel
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
    text = "Мои прочитанные книги",
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun FinishedBookCard(
    book: Book
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
        .background(
            color = Color(0xFFE7E7E7),
            shape = RoundedCornerShape(4.dp)
        )
        .drawRightBorder(
            strokeWidth = 20.dp,
            color = Color(0xFF00A45F),
            shape = RoundedCornerShape(4.dp)
        ),
    verticalAlignment = Alignment.CenterVertically
) {
    if (book.coverUrl.isNotEmpty()) {
        GlideImage(
            modifier = Modifier
                .size(60.dp)
                .padding(8.dp),
            model = book.coverUrl,
            contentDescription = "Book image",
            loading = placeholder(R.drawable.ic_default_book),
            failure = placeholder(R.drawable.ic_default_book)
        )
    } else {
        Image(
            modifier = Modifier
                .size(60.dp)
                .padding(8.dp),
            painter = painterResource(id = R.drawable.ic_default_book),
            contentDescription = ""
        )
    }

    Column(
        modifier = Modifier.padding(
            top = 8.dp,
            start = 8.dp,
            bottom = 8.dp
        ),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            modifier = Modifier.fillMaxWidth(0.8f),
            text = book.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    end = 16.dp
                )
        ) {
            Text(
                modifier = Modifier.weight(1f, fill = false),
                text = book.authorName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(text = ", ${book.publishYear}")
        }

        Text(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp, end = 16.dp),
            text = book.finishedDate.split(" ").first()
        )
    }
}

@Composable
private fun NoFinishedBooksContent() = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = "У Вас еще нет книг, которые вы прочитали",
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
private fun FinishedBooksPreview() = BookTrackerTheme {
    FinishedBooksContent(
        finishedBooks = listOf(
            Book(
                title = "Book A",
                authorName = "Author A",
                publishYear = "2007",
                finishedDate = "27.07.2024"
            ),
            Book(
                title = "Book B asdf asdf asdfasdfasdf asdfasdfasdfasasdfasdff",
                authorName = "Author B asdfadfasdfasdf sadfasdfasdfasdf",
                publishYear = "2008",
                finishedDate = "13.05.2024"
            ),
            Book(
                title = "Book C",
                authorName = "Author C",
                publishYear = "2009",
                finishedDate = "23.06.2024"
            ),
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun NoFinishedBooksPreview() = BookTrackerTheme {
    NoFinishedBooksContent()
}