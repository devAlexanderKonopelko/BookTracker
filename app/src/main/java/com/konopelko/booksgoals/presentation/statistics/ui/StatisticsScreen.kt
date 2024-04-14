package com.konopelko.booksgoals.presentation.statistics.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun StatisticsScreen() {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = "Statistics Screen",
        textAlign = TextAlign.Center
    )
}