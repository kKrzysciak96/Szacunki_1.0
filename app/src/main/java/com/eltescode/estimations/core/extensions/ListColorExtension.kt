package com.eltescode.estimations.core.extensions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun List<Color>.gradientBackground() =
    Brush.linearGradient(colors = this, start = Offset.Zero, end = Offset.Infinite)