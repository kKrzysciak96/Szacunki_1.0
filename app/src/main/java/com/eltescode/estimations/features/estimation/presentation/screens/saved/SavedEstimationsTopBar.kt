package com.eltescode.estimations.features.estimation.presentation.screens.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eltescode.estimations.ui.theme.colorDarkGreen
import com.eltescode.estimations.R

@Composable
fun SavedEstimationsTopBar() {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .fillMaxWidth()
            .background(colorDarkGreen),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.hint19),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5,
            color = Color.Black,
            modifier = Modifier.padding(10.dp)
        )
    }
}