package com.example.szacunki.features.estimation.presentation.screens.estimation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.szacunki.R
import com.example.szacunki.core.calculations.color2
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable

@Composable
fun BottomInfoBar(estimation: State<EstimationDisplayable>, memoState: MutableState<Boolean>) {

    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        backgroundColor = color2
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(color = color2), contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Oddział nr.: ${estimation.value.sectionNumber} Date: ${estimation.value.date.time}",
                    style = MaterialTheme.typography.subtitle1
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_note),
                    contentDescription = null,
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .background(color2)
                        .clickable { memoState.value = true }
                )

            }
        }
    }
}


