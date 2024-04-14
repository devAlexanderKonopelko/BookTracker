package com.konopelko.booksgoals.presentation.common.component.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.konopelko.booksgoals.presentation.common.theme.BooksGoalsAppTheme

@Composable
fun BaseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        onClick = onClick
    ) {
        if(isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = text)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BaseButtonPreview() = BooksGoalsAppTheme {
    BaseButton(
        text = "Button text",
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun BaseButtonLoadingPreview() = BooksGoalsAppTheme {
    BaseButton(
        text = "Button text",
        isLoading = true,
        onClick = {}
    )
}