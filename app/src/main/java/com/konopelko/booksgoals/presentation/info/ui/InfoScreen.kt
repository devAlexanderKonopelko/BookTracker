package com.konopelko.booksgoals.presentation.info.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.konopelko.booksgoals.presentation.common.theme.BookTrackerTheme

@Composable
fun InfoScreen() = Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(text = "Some info about the app")
}

@Preview(showBackground = true)
@Composable
private fun InfoScreenPreview() = BookTrackerTheme {
    InfoScreen()
}