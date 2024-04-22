package com.konopelko.booksgoals.presentation.achievements.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AchievementsScreen() {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = "Мои достижения",
        textAlign = TextAlign.Center
    )
}