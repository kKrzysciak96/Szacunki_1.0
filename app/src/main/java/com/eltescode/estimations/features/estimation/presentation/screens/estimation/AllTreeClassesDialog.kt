package com.eltescode.estimations.features.estimation.presentation.screens.estimation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eltescode.estimations.R
import com.eltescode.estimations.core.composable.ConfirmIconButton
import com.eltescode.estimations.features.estimation.presentation.model.EstimationDisplayable
import com.eltescode.estimations.ui.theme.colorDarkGreen

@Composable
fun AllTreeClassesDialog(
    estimation: EstimationDisplayable,
    treeIndex: Int,
    diameterIndex: Int,
    updateEstimation: (EstimationDisplayable) -> Unit,
    onDismissRequest: () -> Unit
) {
    val verticalScrollState = rememberScrollState()
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Card {
                Column(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val title = "${estimation.trees[treeIndex].name}: " +
                            estimation.trees[treeIndex].treeRows[diameterIndex].diameter
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                    )
                    AllClassesTitleRow()
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .verticalScroll(state = verticalScrollState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        estimation.trees[treeIndex].treeRows[diameterIndex].treeQualityClasses.run {
                            ClassParametersRow(
                                treeQualityClassQuantity = class1,
                                name = ButtonId.CLASS1.description,
                                treeIndex = treeIndex,
                                diameterIndex = diameterIndex,
                                estimation = estimation,
                                updateEstimation = updateEstimation,
                                buttonId = ButtonId.CLASS1
                            )
                            ClassParametersRow(
                                treeQualityClassQuantity = class2,
                                name = ButtonId.CLASS2.description,
                                treeIndex = treeIndex,
                                diameterIndex = diameterIndex,
                                estimation = estimation,
                                updateEstimation = updateEstimation,
                                buttonId = ButtonId.CLASS2
                            )
                            ClassParametersRow(
                                treeQualityClassQuantity = class3,
                                name = ButtonId.CLASS3.description,
                                treeIndex = treeIndex,
                                diameterIndex = diameterIndex,
                                estimation = estimation,
                                updateEstimation = updateEstimation,
                                buttonId = ButtonId.CLASS3
                            )
                            ClassParametersRow(
                                treeQualityClassQuantity = classA,
                                name = ButtonId.CLASSA.description,
                                treeIndex = treeIndex,
                                diameterIndex = diameterIndex,
                                estimation = estimation,
                                updateEstimation = updateEstimation,
                                buttonId = ButtonId.CLASSA
                            )
                            ClassParametersRow(
                                treeQualityClassQuantity = classB,
                                name = ButtonId.CLASSB.description,
                                treeIndex = treeIndex,
                                diameterIndex = diameterIndex,
                                estimation = estimation,
                                updateEstimation = updateEstimation,
                                buttonId = ButtonId.CLASSB
                            )
                            ClassParametersRow(
                                treeQualityClassQuantity = classC,
                                name = ButtonId.CLASSC.description,
                                treeIndex = treeIndex,
                                diameterIndex = diameterIndex,
                                estimation = estimation,
                                updateEstimation = updateEstimation,
                                buttonId = ButtonId.CLASSC
                            )
                        }
                    }
                }

            }
            ConfirmIconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 10.dp)
                    .size(30.dp),
                onClick = onDismissRequest
            )
        }
    }
}

@Composable
private fun AllClassesTitleRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorDarkGreen),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.hint48))
        Text(text = stringResource(id = R.string.hint12))
    }
}

@Composable
private fun ClassParametersRow(
    treeQualityClassQuantity: Int,
    name: String,
    treeIndex: Int,
    diameterIndex: Int,
    estimation: EstimationDisplayable,
    buttonId: ButtonId,
    updateEstimation: (EstimationDisplayable) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = name, modifier = Modifier
                .width(90.dp)
                .padding(5.dp)
        )
        CustomIdButton(
            treeIndex = treeIndex,
            diameterIndex = diameterIndex,
            estimation = estimation,
            updateEstimation = updateEstimation,
            buttonId = buttonId,
            addMode = false
        )
        Text(
            text = treeQualityClassQuantity.toString(),
            modifier = Modifier
                .width(50.dp)
                .padding(5.dp), textAlign = TextAlign.Center
        )
        CustomIdButton(
            treeIndex = treeIndex,
            diameterIndex = diameterIndex,
            estimation = estimation,
            updateEstimation = updateEstimation,
            buttonId = buttonId,
            addMode = true
        )
    }
}


