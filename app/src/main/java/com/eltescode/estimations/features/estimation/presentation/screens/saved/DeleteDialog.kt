package com.eltescode.estimations.features.estimation.presentation.screens.saved

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eltescode.estimations.features.estimation.presentation.model.EstimationDisplayable
import com.eltescode.estimations.ui.theme.colorDarkGreen
import com.eltescode.estimations.R

@Composable
fun DeleteDialog(
    estimation: EstimationDisplayable,
    deleteEstimation: (EstimationDisplayable) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.hint16),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                    Row(modifier = Modifier.padding(20.dp)) {
                        OutlinedButton(
                            modifier = Modifier.padding(10.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                            border = BorderStroke(width = 1.dp, color = Color.Black),
                            onClick = {
                                deleteEstimation(estimation)
                                onDismiss()
                            })
                        {
                            Text(
                                text = stringResource(id = R.string.hint17),
                                style = MaterialTheme.typography.h5
                            )
                        }
                        OutlinedButton(
                            modifier = Modifier.padding(10.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorDarkGreen),
                            border = BorderStroke(width = 1.dp, color = Color.DarkGray),
                            onClick = { onDismiss() })
                        {
                            Text(
                                text = stringResource(id = R.string.hint18),
                                style = MaterialTheme.typography.h5
                            )
                        }
                    }
                }
            }
        }
    }
}