package com.example.szacunki.features.estimation.presentation.screens.estimation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.szacunki.R
import com.example.szacunki.core.extensions.createEstimationToUpdateMemo
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.ui.theme.color2

@Composable
fun MemoDialog(
    estimation: EstimationDisplayable,
    updateEstimation: (EstimationDisplayable) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card {
            Box(contentAlignment = Alignment.Center) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = stringResource(id = R.string.hint8),
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(5.dp)
                    )
                    OutlinedTextField(
                        value = estimation.memo,
                        onValueChange = {
                            updateEstimation(
                                estimation.createEstimationToUpdateMemo(
                                    memo = it
                                )
                            )
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