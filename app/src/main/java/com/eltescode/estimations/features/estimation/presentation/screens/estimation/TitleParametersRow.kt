package com.eltescode.estimations.features.estimation.presentation.screens.estimation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eltescode.estimations.ui.theme.colorDarkGreen
import com.eltescode.estimations.R
import com.eltescode.estimations.ui.theme.colorWhite

@Composable
fun TitleParametersRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorDarkGreen),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.hint11), color = colorWhite)
        Text(text = stringResource(id = R.string.hint12), color = colorWhite)
        Text(text = stringResource(id = R.string.hint13), color = colorWhite)
    }
}