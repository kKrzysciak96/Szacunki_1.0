package com.example.szacunki.features.estimation.presentation.screens.estimation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.szacunki.R
import com.example.szacunki.ui.theme.color2

@Composable
fun TitleParametersRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color2),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.hint11))
        Text(text = stringResource(id = R.string.hint12))
        Text(text = stringResource(id = R.string.hint13))
    }
}