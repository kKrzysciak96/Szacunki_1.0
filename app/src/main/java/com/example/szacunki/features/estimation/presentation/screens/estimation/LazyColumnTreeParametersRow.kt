package com.example.szacunki.features.estimation.presentation.screens.estimation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.szacunki.R
import com.example.szacunki.core.calculations.color2
import com.example.szacunki.core.calculations.createEstimationToUpdateTreeHeight
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable


@Composable
fun TitleParametersRow(index: Int = 0) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color2),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Średnica INDEX: $index")
        Text(text = "Liczba Drzew")
        Text(text = "Wysokość")
    }
}

@Composable
fun LazyColumnTreeParametersRow(
    estimation: State<EstimationDisplayable>,
    viewModel: EstimationViewModel,
    treeIndex: MutableState<Int>,
    diameterIndexState: MutableState<Int>,
    classesDialogState: MutableState<Boolean>
) {
    LazyColumn(
        content = {
            itemsIndexed(estimation.value.trees[treeIndex.value].treeRows) { itemIndex, item ->
                TreeParametersRow(
                    diameter = item.diameter,
                    treeIndex = treeIndex.value,
                    diameterIndex = itemIndex,
                    diameterIndexState = diameterIndexState,
                    estimation = estimation,
                    viewModel = viewModel,
                    classesDialogState = classesDialogState
                )
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 70.dp)
    )
}

@Composable
fun TreeParametersRow(
    diameter: String,
    treeIndex: Int,
    diameterIndex: Int,
    diameterIndexState: MutableState<Int>,
    estimation: State<EstimationDisplayable>,
    viewModel: EstimationViewModel,
    classesDialogState: MutableState<Boolean>
) {
    val isHeightDropdownMenuVisible = rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = diameter,
            modifier = Modifier
                .width(70.dp)
                .padding(4.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1
        )
        CustomIdButton(
            treeIndex = treeIndex,
            diameterIndex = diameterIndex,
            estimation = estimation,
            viewModel = viewModel,
            buttonId = ButtonId.CLASS3,
            addMode = false
        )
        Text(
            text = estimation.value.trees[treeIndex].treeRows[diameterIndex].treeQualityClasses.class3.toString(),

            modifier = Modifier
                .width(50.dp)
                .clickable {
                    diameterIndexState.value = diameterIndex
                    classesDialogState.value = true
                }, textAlign = TextAlign.Center
        )
        CustomIdButton(
            treeIndex = treeIndex,
            diameterIndex = diameterIndex,
            estimation = estimation,
            viewModel = viewModel,
            buttonId = ButtonId.CLASS3,
            addMode = true,
            modifier = Modifier.padding(end = 20.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .width(50.dp)
                .clickable {
                    isHeightDropdownMenuVisible.value = true
                }) {
            Text(
                text = estimation.value.trees[treeIndex].treeRows[diameterIndex].height.toString()
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_dropdown_arrow),
                contentDescription = null
            )
            HeightDropdownMenu(
                isHeightDropdownMenuVisible = isHeightDropdownMenuVisible,
                estimation = estimation,
                viewModel = viewModel,
                diameterIndex = diameterIndex,
                treeIndex = treeIndex,
                onDismiss = {
                    isHeightDropdownMenuVisible.value = false
                })
        }
    }
}


@Composable
fun HeightDropdownMenu(
    isHeightDropdownMenuVisible: State<Boolean>,
    estimation: State<EstimationDisplayable>,
    viewModel: EstimationViewModel,
    diameterIndex: Int,
    treeIndex: Int,
    onDismiss: () -> Unit,
) {
    DropdownMenu(expanded = isHeightDropdownMenuVisible.value, onDismissRequest = { onDismiss() }) {
        (0..35).forEach { height ->
            DropdownMenuItem(onClick = {
                val newEstimation = createEstimationToUpdateTreeHeight(
                    estimation = estimation,
                    height = height,
                    diameterIndex = diameterIndex,
                    treeIndex = treeIndex
                )
                viewModel.updateEstimationFlow(newEstimation)
                onDismiss()
            }) {
                Text(text = "$height m")
            }
        }
    }
}