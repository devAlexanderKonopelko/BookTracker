package com.konopelko.booksgoals.presentation.wishes.ui

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konopelko.booksgoals.R.drawable
import com.konopelko.booksgoals.domain.model.book.Book
import com.konopelko.booksgoals.presentation.common.theme.BooksGoalsAppTheme
import com.konopelko.booksgoals.presentation.common.theme.Typography
import com.konopelko.booksgoals.presentation.common.theme.backgroundCream
import com.konopelko.booksgoals.presentation.common.utils.border.drawRightBorder
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.HideWishBookDeletedMessage
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.OnArgsReceived
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.ResetNavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.WishesNavigationIntent
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.WishesNavigationIntent.NavigateToSearchBooksScreen
import com.konopelko.booksgoals.presentation.wishes.WishesIntent.WishesNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.wishes.WishesViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun WishesScreen(
    viewModel: WishesViewModel = getViewModel(),
    onNavigate: (WishesNavigationIntent) -> Unit,
    args: Boolean?
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var showWishBookMenuOptions by remember { mutableStateOf(false) }
    var menuOptionBook by remember { mutableStateOf(Book()) }

    when {
        uiState.wishesBooks.isNotEmpty() -> WishesContent(
            wishesBooks = uiState.wishesBooks,
            onNavigate = onNavigate,
            onWishBookMenuClicked = { book ->
                showWishBookMenuOptions = true
                menuOptionBook = book
            }
        )
        else -> NoWishesContent(onNavigate)
    }
    
    args?.let {
        LaunchedEffect("args_key") {
            viewModel.acceptIntent(OnArgsReceived(it))
        }
    }

    if(showWishBookMenuOptions) {
        WishBookOptionsMenu(
            book = menuOptionBook,
            onOptionClicked = viewModel::acceptIntent,
            onDismiss = { showWishBookMenuOptions = false }
        )
    }

    if(uiState.shouldNavigateToAddGoalScreen) {
        LaunchedEffect("navigate_key") {
            onNavigate(NavigateToAddGoalScreen(uiState.bookToStartGoalWith))
            viewModel.acceptIntent(ResetNavigateToAddGoalScreen)
        }
    }

    if(uiState.showWishBookDeletedMessage) {
        LaunchedEffect("toast_key") {
            Toast.makeText(context, "Книга успешно удалена!", Toast.LENGTH_SHORT).show()
            viewModel.acceptIntent(HideWishBookDeletedMessage)
        }
    }
}

@Composable
private fun WishesContent(
    wishesBooks: List<Book>,
    onNavigate: (WishesNavigationIntent) -> Unit,
    onWishBookMenuClicked: (Book) -> Unit
) = Box(
    modifier = Modifier.fillMaxSize(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundCream),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WishesHeader()

        WishesBooksListContent(
            wishesBooks = wishesBooks,
            onWishBookMenuClicked = onWishBookMenuClicked
        )
    }

    FloatingActionButton(
        modifier = Modifier
            .padding(
                end = 16.dp,
                bottom = 16.dp
            )
            .align(Alignment.BottomEnd),
        onClick = { onNavigate(NavigateToSearchBooksScreen) }
    ) {
        Icon(Filled.Add,"")
    }
}

@Composable
private fun WishesBooksListContent(
    wishesBooks: List<Book>,
    onWishBookMenuClicked: (Book) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(
                top = 24.dp,
                start = 16.dp,
                end = 16.dp
            )
    ) {
        items(wishesBooks) { book ->
            WishBookCard(
                book = book,
                onWishBookMenuClicked = onWishBookMenuClicked
            )
        }
    }
}

@Composable
private fun WishBookCard(
    book: Book,
    onWishBookMenuClicked: (Book) -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
        .background(
            color = Color.LightGray,
            shape = RoundedCornerShape(12.dp)
        )
        .drawRightBorder(
            strokeWidth = 20.dp,
            color = Color.Blue,
            shape = RoundedCornerShape(12.dp)
        ),
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

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.8f),
                text = book.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            IconButton(
                onClick = { onWishBookMenuClicked(book) }
            ) {
                Icon(
                    painter = painterResource(id = drawable.ic_options_menu),
                    contentDescription = ""
                )
            }
        }

        Row {
            Text(text = book.authorName + ",")

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = book.publishYear
            )
        }
    }
}

@Composable
private fun WishesHeader() = Text(
    modifier = Modifier.padding(top = 16.dp),
    text = "Книги, которые я хочу прочитать",
    style = Typography.headlineLarge,
)

@Composable
private fun NoWishesContent(
    onNavigate: (WishesNavigationIntent) -> Unit,
) = Box(
    modifier = Modifier.fillMaxSize()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Здесь ничего нет",
            textAlign = TextAlign.Center
        )
    }

    FloatingActionButton(
        modifier = Modifier
            .padding(
                end = 16.dp,
                bottom = 16.dp
            )
            .align(Alignment.BottomEnd),
        onClick = { onNavigate(NavigateToSearchBooksScreen) }
    ) {
        Icon(Filled.Add,"")
    }
}

@Preview(showBackground = true)
@Composable
private fun WishesPreview() = BooksGoalsAppTheme {
    WishesContent(
        wishesBooks = listOf(
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
        ),
        onNavigate = {},
        onWishBookMenuClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun NoWishesPreview() = BooksGoalsAppTheme {
    NoWishesContent(onNavigate = {})
}