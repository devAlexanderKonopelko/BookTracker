package com.konopelko.booksgoals.presentation.common.utils.border

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

fun Modifier.drawRightBorder(
    strokeWidth: Dp,
    color: Color,
    shape: Shape = RectangleShape
): Modifier = this
    .clip(shape)
    .drawWithContent {
        val x = size.width - strokeWidth.value / 2
        val y = size.height

        drawContent()
        drawLine(
            color = color,
            start = Offset(x, 0f), // top-right corner
            end = Offset(x, y), // bottom-right corner
            strokeWidth = strokeWidth.value
        )
    }