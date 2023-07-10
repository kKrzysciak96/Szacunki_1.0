package com.example.szacunki.core.composablefunctions

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GradientBackground(
    colorList: List<Color>
): Brush {

    return Brush.linearGradient(colors = colorList, start = Offset.Zero, end = Offset.Infinite)
}