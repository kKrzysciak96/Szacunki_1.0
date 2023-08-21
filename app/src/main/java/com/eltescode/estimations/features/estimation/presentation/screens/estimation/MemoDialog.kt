package com.eltescode.estimations.features.estimation.presentation.screens.estimation

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
import com.eltescode.estimations.core.extensions.createEstimationToUpdateMemo
import com.eltescode.estimations.features.estimation.presentation.model.EstimationDisplayable
import com.eltescode.estimations.ui.theme.colorDarkGreen
import com.eltescode.estimations.R
import com.eltescode.estimations.core.composable.ConfirmIconButton

@Composable
fun MemoDialog(
    estimation: EstimationDisplayable,
    updateEstimation: (EstimationDisplayable) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card {
            Box(contentAlignment = Alignment.Center) {
                ConfirmIconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 10.dp)
                        .size(30.dp),
                    onClick = onDismissRequest
                )
                Column(
                    modifier = Modifier.padding(
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 20.dp
                    )
                ) {
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
                        label = { Text(text = stringResource(id = R.string.hint9)) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colorDarkGreen,
                            focusedLabelColor = colorDarkGreen
                        )
                    )
                }

            }
        }
    }
}

