package com.example.szacunki.features.estimation.presentation.screens.saved

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.szacunki.R
import com.example.szacunki.ui.theme.color2

@Composable
fun SavedEstimationsTopBar() {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = color2
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.hint19),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5
            )
        }
    }
}