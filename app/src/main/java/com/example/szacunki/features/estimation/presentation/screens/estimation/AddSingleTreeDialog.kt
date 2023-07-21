package com.example.szacunki.features.estimation.presentation.screens.estimation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.szacunki.R
import com.example.szacunki.core.extensions.createEstimationToUpdateTreeNames
import com.example.szacunki.core.extensions.getBaseNameList
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.ui.theme.color1
import com.example.szacunki.ui.theme.color2

@Composable
fun AddSingleTreeDialog(
    context: Context,
    estimation: EstimationDisplayable,
    treeIndexState: MutableState<Int>,
    updateEstimation: (EstimationDisplayable) -> Unit,
    onDismissRequest: () -> Unit
) {
    val newTreeName = rememberSaveable { mutableStateOf("") }
    val presentTrees: MutableList<String> = emptyList<String>().toMutableList()

    estimation.trees.forEach {
        presentTrees.add(it.name)
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Card {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.hint1),
                        style = MaterialTheme.typography.h5
                    )
                    OutlinedTextField(
                        value = newTreeName.value,
                        onValueChange = { newTreeName.value = it },
                        label = { Text(text = stringResource(id = R.string.hint2)) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = color2, focusedLabelColor = color2
                        )
                    )
                    OutlinedButton(
                        onClick = {
                            val newEstimation = estimation.createEstimationToUpdateTreeNames(
                                context = context,
                                treeName = newTreeName.value
                            )
                            updateEstimation(newEstimation)
                            treeIndexState.value = estimation.trees.size
                            onDismissRequest()
                        },
                        enabled = newTreeName.value.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.Black,
                            disabledContentColor = Color.Gray,
                            backgroundColor = Color.White,
                            disabledBackgroundColor = Color.LightGray
                        ),
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.hint3),
                            modifier = Modifier,
                            style = MaterialTheme.typography.h5,
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp), thickness = 2.dp
                    )
                    Text(
                        text = stringResource(id = R.string.hint4),
                        style = MaterialTheme.typography.h5
                    )
                    ChooseTree(
                        context = context,
                        presentTrees = presentTrees,
                        estimation = estimation,
                        updateEstimation = updateEstimation,
                        treeIndexState = treeIndexState,
                        onDismissRequest = onDismissRequest,
                    )
                }
            }
        }
    }
}

@Composable
private fun ChooseTree(
    context: Context,
    presentTrees: List<String>,
    estimation: EstimationDisplayable,
    treeIndexState: MutableState<Int>,
    updateEstimation: (EstimationDisplayable) -> Unit,
    onDismissRequest: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val remainingTrees = context.getBaseNameList() - presentTrees.toSet()
        items(remainingTrees) { treeName ->
            val isChecked = rememberSaveable { mutableStateOf(false) }
            Card(
                modifier = Modifier.padding(5.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = 5.dp
            ) {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = if (isChecked.value) color1 else Color.White)
                        .clickable {
                            val newEstimation = estimation.createEstimationToUpdateTreeNames(
                                context = context,
                                treeName = treeName
                            )
                            updateEstimation(newEstimation)
                            treeIndexState.value = estimation.trees.size
                            onDismissRequest()
                        }) {
                    Text(
                        text = treeName,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(5.dp),
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}