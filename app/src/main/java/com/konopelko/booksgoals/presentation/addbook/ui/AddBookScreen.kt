package com.konopelko.booksgoals.presentation.addbook.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.AddBookNavigationIntent
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.AddBookNavigationIntent.NavigateToAddGoalScreen
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnAddBookClicked
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnAuthorNameChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnPagesAmountChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnPublishYearChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookIntent.OnTitleChanged
import com.konopelko.booksgoals.presentation.addbook.AddBookUiState
import com.konopelko.booksgoals.presentation.addbook.AddBookViewModel
import com.konopelko.booksgoals.presentation.common.component.button.BaseButton
import com.konopelko.booksgoals.presentation.common.theme.BooksGoalsAppTheme
import com.konopelko.booksgoals.presentation.common.theme.Typography
import org.koin.androidx.compose.getViewModel

@Composable
fun AddBookScreen(
    viewModel: AddBookViewModel = getViewModel(),
    onNavigate: (AddBookNavigationIntent) -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    AddBookContent(
        uiState = uiState,
        onIntent = viewModel::acceptIntent
    )

    if(uiState.isBookSaved) {
        LaunchedEffect("toast key") {
            Toast.makeText(context, "Book saved successfully!", Toast.LENGTH_SHORT).show()
            onNavigate(NavigateToAddGoalScreen(uiState.book))
        }
    }
}

@Composable
private fun AddBookContent(
    uiState: AddBookUiState,
    onIntent: (AddBookIntent) -> Unit
) = Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        modifier = Modifier.padding(top = 16.dp),
        text = "Adding new book!",
        textAlign = TextAlign.Center,
        style = Typography.titleLarge
    )

    Spacer(modifier = Modifier.weight(1f))

    with(uiState) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = book.title,
            onValueChange = {
                onIntent(OnTitleChanged(bookTitle = it))
            },
            placeholder = { Text(text = "Type book name") },
            isError = isBookTitleError
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = book.authorName,
            onValueChange = {
                onIntent(OnAuthorNameChanged(authorName = it))
            },
            placeholder = { Text(text = "Type author name") },
            isError = isAuthorNameError
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = book.publishYear,
            onValueChange = {
                if(it.isDigitsOnly()) {
                    onIntent(OnPublishYearChanged(publishYear = it))
                }
            },
            placeholder = { Text(text = "Type book publish year") },
            isError = isPublishYearError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = book.pagesAmount,
            onValueChange = {
                if(it.isDigitsOnly()) {
                    onIntent(OnPagesAmountChanged(pagesAmount = it))
                }
            },
            placeholder = { Text(text = "Type book pages amount") },
            isError = isPagesAmountError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.weight(1f))

        BaseButton(
            text = "Add a book",
            enabled = isSaveButtonEnabled,
            isLoading = isSaveButtonLoading,
            onClick = { onIntent(OnAddBookClicked) }
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun AddBookPreview() = BooksGoalsAppTheme {
    AddBookContent(
        uiState = AddBookUiState(),
        onIntent = {}
    )
}