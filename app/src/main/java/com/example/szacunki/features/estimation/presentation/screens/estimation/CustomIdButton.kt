package com.example.szacunki.features.estimation.presentation.screens.estimation

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.szacunki.R
import com.example.szacunki.core.calculations.color1
import com.example.szacunki.core.calculations.createEstimationToUpdateQuantities
import com.example.szacunki.core.calculations.performAddition
import com.example.szacunki.core.calculations.performSubtraction
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable

@Composable
fun CustomIdButton(
    treeIndex: Int,
    diameterIndex: Int,
    estimation: State<EstimationDisplayable>,
    viewModel: EstimationViewModel,
    buttonId: ButtonId,
    addMode: Boolean,
    modifier: Modifier = Modifier
) {
    Button(onClick = {
        val newQuantity = if (addMode) {
            performAddition(estimation, treeIndex, diameterIndex, buttonId)
        } else {
            performSubtraction(estimation, treeIndex, diameterIndex, buttonId)
        }
        val newEstimation =
            createEstimationToUpdateQuantities(
                newQuantity,
                estimation,
                treeIndex,
                diameterIndex,
                buttonId
            )
        viewModel.updateEstimationFlow(newEstimation)
    }, colors = ButtonDefaults.buttonColors(backgroundColor = color1), modifier = modifier) {
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