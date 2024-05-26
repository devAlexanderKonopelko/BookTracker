package com.konopelko.booksgoals.presentation.goaldetails.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.konopelko.booksgoals.R
import com.konopelko.booksgoals.presentation.common.component.button.BaseButton
import com.konopelko.booksgoals.presentation.common.theme.BookTrackerTheme

@Composable
fun ProgressMarkDialog(
    modifier: Modifier = Modifier,
    pagesLeftAmount: Int,
    onDismissRequest: () -> Unit,
    onSaveProgressClicked: (Int) -> Unit
) = Dialog(
    onDismissRequest = onDismissRequest,
    properties = DialogProperties(dismissOnClickOutside = true)
) {
    val pagesRead = remember { mutableStateOf(0) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Сколько страниц вы прочитали сегодня?",
                textAlign = TextAlign.Center,
            )

            Card(
                modifier = Modifier
                    .width(100.dp)
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(16.dp),
                colors = CardColors(
                    containerColor = Color(0xFFF7F7F7),
                    contentColor = CardDefaults.cardColors().contentColor,
                    disabledContainerColor = CardDefaults.cardColors().disabledContainerColor,
                    disabledContentColor = CardDefaults.cardColors().disabledContentColor
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(3f))

                    Text(
                        text = "${pagesRead.value}",
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Column {
                        IconButton(
                            onClick = {
                                if (pagesRead.value < pagesLeftAmount) pagesRead.value++
                            }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_arrow_top),
                                contentDescription = ""
                            )
                        }
                        IconButton(
                            onClick = {
                                if (pagesRead.value > 0) pagesRead.value--
                            }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_arrow_down),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }

            BaseButton(
                modifier = Modifier.padding(top = 16.dp),
                text = "Сохранить",
                onClick = { onSaveProgressClicked(pagesRead.value) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressMarkDialogPreview() = BookTrackerTheme {
    ProgressMarkDialog(
        modifier = Modifier
            .width(400.dp),
        pagesLeftAmount = 1,
        onDismissRequest = {},
        onSaveProgressClicked = {}
    )
}