package com.example.szacunki.features.estimation.presentation.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.szacunki.core.calculations.color1
import com.example.szacunki.features.estimation.presentation.model.baseNameList
import com.example.szacunki.features.estimation.presentation.screens.destinations.EstimationScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(route = "TreeSelectionScreen")
@Composable
fun TreeSelectionScreen(
    sectionNumber: String?,
    navigator: DestinationsNavigator
) {
    val treeList = rememberSaveable { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Wybierz drzewa",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(baseNameList) { treeName ->
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
                                isChecked.value = !isChecked.value
                                if (isChecked.value) {
                                    val newList = treeList.value.toMutableList()
                                    newList.add(treeName)
                                    treeList.value = newList
                                } else {
                                    val newList = treeList.value.toMutableList()
                                    newList.remove(treeName)
                                    treeList.value = newList
                                }
                            }) {
                        Text(
                            text = treeName,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
            }
        }
        if (sectionNumber != null) {
            OutlinedButton(onClick = {
                val navArg = EstimationScreenDestination.NavArgs(
                    id = null,
                    sectionNumber = sectionNumber,
                    treeNames = treeList.value.toTypedArray()
                )
                navigator.popBackStack()
                navigator.navigate(EstimationScreenDestination(navArg))
            }, enabled = treeList.value.isNotEmpty(), modifier = Modifier.padding(top = 20.dp)) {
                Text(
                    text = "Rozpocznij pracę",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5
                )
            }
        }
    }
}
