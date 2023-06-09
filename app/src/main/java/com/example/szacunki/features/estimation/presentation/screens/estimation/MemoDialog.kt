package com.example.szacunki.features.estimation.presentation.screens.estimation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.szacunki.core.calculations.color2
import com.example.szacunki.core.calculations.updateEstimationMemo
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable

@Composable
fun MemoDialog(
    estimation: State<EstimationDisplayable>,
    viewModel: EstimationViewModel,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = { onDismiss() }) {
        Card() {
            Box(contentAlignment = Alignment.Center) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Notatka służbowa:",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(5.dp)
                    )
                    OutlinedTextField(
                        value = estimation.value.memo,
                        onValueChange = {
                            val newEstimation =
                                updateEstimationMemo(estimation = estimation, memo = it)
                            viewModel.updateEstimationFlow(newEstimation)
                        },
                        modifier = Modifier.size(300.dp),
                        label = { Text(text = "Wpisz Notatke") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = color2,
                            focusedLabelColor = color2
                        )
                    )
                }
            }

        }

    }
}