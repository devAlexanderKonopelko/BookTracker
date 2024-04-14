package com.konopelko.booksgoals.presentation.books.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun BooksScreen() {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = "Books Screen",
        textAlign = TextAlign.Center
    )
}