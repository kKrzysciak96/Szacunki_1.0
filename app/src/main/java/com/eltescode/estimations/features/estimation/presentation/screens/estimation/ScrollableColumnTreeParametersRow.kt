package com.eltescode.estimations.features.estimation.presentation.screens.estimation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eltescode.estimations.core.extensions.createEstimationToUpdateTreeHeight
import com.eltescode.estimations.features.estimation.presentation.model.EstimationDisplayable
import com.eltescode.estimations.R

@Composable
fun ScrollableColumnTreeParametersRow(
    estimation: EstimationDisplayable,
    treeIndex: MutableState<Int>,
    diameterIndexState: MutableState<Int>,
    classesDialogState: MutableState<Boolean>,
    updateEstimation: (EstimationDisplayable) -> Unit,
) {
    val verticalScrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 70.dp)
            .verticalScroll(state = verticalScrollState)
    ) {
        estimation.trees[treeIndex.value].treeRows.forEachIndexed { itemIndex, item ->
            TreeParametersRow(
                diameter = item.diameter,
                treeIndex = treeIndex.value,
                diameterIndex = itemIndex,
                diameterIndexState = diameterIndexState,
                estimation = estimation,
                updateEstimation = updateEstimation,
                classesDialogState = classesDialogState
            )
        }
    }
}

@Composable
private fun TreeParametersRow(
    diameter: String,
    treeIndex: Int,
    diameterIndex: Int,
    diameterIndexState: MutableState<Int>,
    estimation: EstimationDisplayable,
    updateEstimation: (EstimationDisplayable) -> Unit,
    classesDialogState: MutableState<Boolean>
) {
    val isHeightDropdownMenuVisible = rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SingleRowDiameterText(
            diameter = diameter
        )
        SingleRowSubtractionButton(
            treeIndex = treeIndex,
            diameterIndex = diameterIndex,
            estimation = estimation,
            updateEstimation = updateEstimation,
            buttonId = ButtonId.CLASS3,
        )
        SingleRowQuantityText(
            classesDialogState = classesDialogState,
            treeIndex = treeIndex,
            diameterIndex = diameterIndex,
            diameterIndexState = diameterIndexState,
            estimation = estimation
        )
        SingleRowAddButton(
            treeIndex = treeIndex,
            diameterIndex = diameterIndex,
            estimation = estimation,
            updateEstimation = updateEstimation,
            buttonId = ButtonId.CLASS3,
            modifier = Modifier.padding(end = 20.dp)
        )
        SingleRowHeightText(
            treeIndex = treeIndex,
            diameterIndex = diameterIndex,
            estimation = estimation,
            isHeightDropdownMenuVisible = isHeightDropdownMenuVisible,
            updateEstimation = updateEstimation
        )
    }
}

@Composable
private fun SingleRowDiameterText(diameter: String) {
    Text(
        text = diameter,
        modifier = Modifier
            .width(70.dp)
            .padding(4.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.body1
    )
}

@Composable
fun SingleRowQuantityText(
    treeIndex: Int,
    diameterIndex: Int,
    diameterIndexState: MutableState<Int>,
    estimation: EstimationDisplayable,
    classesDialogState: MutableState<Boolean>
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .size(50.dp)
        .clickable {
            diameterIndexState.value = diameterIndex
            classesDialogState.value = true
        }) {
        Text(
            text = estimation.trees[treeIndex].treeRows[diameterIndex].treeQualityClasses.class3.toString(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SingleRowSubtractionButton(
    treeIndex: Int,
    diameterIndex: Int,
    estimation: EstimationDisplayable,
    buttonId: ButtonId,
    updateEstimation: (EstimationDisplayable) -> Unit
) {
    CustomIdButton(
        treeIndex = treeIndex,
        diameterIndex = diameterIndex,
        estimation = estimation,
        updateEstimation = updateEstimation,
        buttonId = buttonId,
        addMode = false
    )
}

@Composable
fun SingleRowAddButton(
    treeIndex: Int,
    diameterIndex: Int,
    estimation: EstimationDisplayable,
    buttonId: ButtonId,
    updateEstimation: (EstimationDisplayable) -> Unit,
    modifier: Modifier = Modifier
) {
    CustomIdButton(
        treeIndex = treeIndex,
        diameterIndex = diameterIndex,
        estimation = estimation,
        updateEstimation = updateEstimation,
        buttonId = buttonId,
        addMode = true,
        modifier = modifier
    )
}

@Composable
fun SingleRowHeightText(
    treeIndex: Int,
    diameterIndex: Int,
    estimation: EstimationDisplayable,
    isHeightDropdownMenuVisible: MutableState<Boolean>,
    updateEstimation: (EstimationDisplayable) -> Unit

) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .size(50.dp)
        .clickable {
            isHeightDropdownMenuVisible.value = true
        }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            Text(
                text = estimation.trees[treeIndex].treeRows[diameterIndex].height.toString()
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_dropdown_arrow),
                contentDescription = null
            )
            HeightDropdownMenu(isHeightDropdownMenuVisible = isHeightDropdownMenuVisible,
                estimation = estimation,
                updateEstimation = updateEstimation,
                diameterIndex = diameterIndex,
                treeIndex = treeIndex,
                onDismissRequest = {
                    isHeightDropdownMenuVisible.value = false
                })
        }
    }
}

@Composable
private fun HeightDropdownMenu(
    isHeightDropdownMenuVisible: State<Boolean>,
    estimation: EstimationDisplayable,
    diameterIndex: Int,
    treeIndex: Int,
    updateEstimation: (EstimationDisplayable) -> Unit,
    onDismissRequest: () -> Unit,
) {
    DropdownMenu(
        expanded = isHeightDropdownMenuVisible.value,
        onDismissRequest = { onDismissRequest() }) {
        (0..35).forEach { height ->
            DropdownMenuItem(
                onClick = {
                    val newEstimation = estimation
                        .createEstimationToUpdateTreeHeight(
                            height = height,
                            diameterIndex = diameterIndex,
                            treeIndex = treeIndex
                        )
                    updateEstimation(newEstimation)
                    onDismissRequest()
                }) {
                Text(text = stringResource(id = R.string.hint10, height.toString()))
            }
        }
    }
}