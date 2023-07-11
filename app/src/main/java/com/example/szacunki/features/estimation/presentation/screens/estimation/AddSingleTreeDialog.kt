package com.example.szacunki.features.estimation.presentation.screens.estimation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.szacunki.core.calculations.color1
import com.example.szacunki.core.calculations.color2
import com.example.szacunki.core.calculations.createEstimationToUpdateTreeNames
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.features.estimation.presentation.model.baseNameList
import kotlinx.coroutines.CoroutineScope

@Composable
fun AddSingleTreeDialog(
    viewModel: EstimationViewModel,
    estimation: State<EstimationDisplayable>,
    treeIndexState: MutableState<Int>,
    listState: LazyListState,
    scope: CoroutineScope,
    onDismissRequest: () -> Unit
) {
    val newTreeName = rememberSaveable { mutableStateOf("") }
    val presentTrees: MutableList<String> = mutableListOf<String>()
    estimation.value.trees.forEach {
        presentTrees.add(it.name)
    }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Box(
            modifier = Modifier,

            contentAlignment = Alignment.Center
        ) {
            Card() {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Wpisz nazwę i dodaj", style = MaterialTheme.typography.h5)
                    OutlinedTextField(
                        value = newTreeName.value,
                        onValueChange = { newTreeName.value = it },
                        label = {
                            Text(
                                text = "Wpisz nazwę drzewa"
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = color2,
                            focusedLabelColor = color2
                        )
                    )
                    OutlinedButton(
                        onClick = {
                            val newEstimation = createEstimationToUpdateTreeNames(
                                estimation = estimation,
                                treeName = newTreeName.value
                            )
                            viewModel.updateEstimationFlow(newEstimation)

                            treeIndexState.value = estimation.value.trees.size

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
                            text = "Dodaj",
                            modifier = Modifier,
                            style = MaterialTheme.typography.h5,
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        thickness = 2.dp
                    )
                    Text(text = "Lub wybierz z poniższych", style = MaterialTheme.typography.h5)
                    ChooseTree(
                        presentTrees = presentTrees,
                        estimation = estimation,
                        viewModel = viewModel,
                        treeIndexState = treeIndexState,
                        onDismissRequest = onDismissRequest,
                        listState = listState,
                        scope = scope
                    )
                }
            }
        }
    }
}

@Composable
fun ChooseTree(
    presentTrees: List<String>,
    estimation: State<EstimationDisplayable>,
    viewModel: EstimationViewModel,
    treeIndexState: MutableState<Int>,
    listState: LazyListState,
    scope: CoroutineScope,
    onDismissRequest: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        val remainingTrees = baseNameList - presentTrees.toSet()
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
                            val newEstimation = createEstimationToUpdateTreeNames(
                                estimation = estimation,
                                treeName = treeName
                            )
                            viewModel.updateEstimationFlow(newEstimation)
                            treeIndexState.value = estimation.value.trees.size
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