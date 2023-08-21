package com.eltescode.estimations.features.estimation.presentation.screens.estimation

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.eltescode.estimations.core.extensions.createEstimationToUpdateQuantities
import com.eltescode.estimations.core.extensions.performAddition
import com.eltescode.estimations.core.extensions.performSubtraction
import com.eltescode.estimations.features.estimation.presentation.model.EstimationDisplayable
import com.eltescode.estimations.ui.theme.colorGreen
import com.eltescode.estimations.R
import com.eltescode.estimations.ui.theme.colorWhite

@Composable
fun CustomIdButton(
    treeIndex: Int,
    diameterIndex: Int,
    estimation: EstimationDisplayable,
    buttonId: ButtonId,
    addMode: Boolean,
    modifier: Modifier = Modifier,
    updateEstimation: (EstimationDisplayable) -> Unit,
) {
    Button(
        onClick = {
            val newQuantity = if (addMode) {
                estimation.performAddition(
                    treeIndex = treeIndex,
                    diameterIndex = diameterIndex,
                    buttonId = buttonId
                )
            } else {
                estimation.performSubtraction(
                    treeIndex = treeIndex,
                    diameterIndex = diameterIndex,
                    buttonId = buttonId
                )
            }
            val newEstimation =
                estimation.createEstimationToUpdateQuantities(
                    newQuantity = newQuantity,
                    treeIndex = treeIndex,
                    diameterIndex = diameterIndex,
                    buttonId = buttonId
                )
            updateEstimation(newEstimation)
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorGreen,
            contentColor = colorWhite
        ),
        modifier = modifier
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(
                id = if (addMode) {
                    R.drawable.ic_plus
                } else {
                    R.drawable.ic_minus
                }
            ),
            contentDescription = null
        )
    }
}

enum class ButtonId(val description: String) {
    CLASS1("1-WA"),
    CLASS2("2-WB,S1"),
    CLASS3("3-WC,WD,S2,S3,S4"),
    CLASSA("SPEC. A"),
    CLASSB("SPEC. B"),
    CLASSC("SPEC. C/S")
}