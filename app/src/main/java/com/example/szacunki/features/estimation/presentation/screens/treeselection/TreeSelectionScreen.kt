package com.example.szacunki.features.estimation.presentation.screens.treeselection


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.szacunki.core.extensions.getBaseNameList
import com.example.szacunki.core.extensions.gradientBackground
import com.example.szacunki.destinations.EstimationScreenDestination
import com.example.szacunki.ui.theme.brushList1
import com.example.szacunki.ui.theme.color4
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(route = "TreeSelectionScreen")
@Composable
fun TreeSelectionScreen(
    sectionNumber: String?,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val treeList = rememberSaveable { mutableStateOf(listOf<String>()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brushList1.gradientBackground()
            ),
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
            items(context.getBaseNameList()) { treeName ->
                val isChecked = rememberSaveable { mutableStateOf(false) }

                Card(
                    modifier = Modifier.padding(5.dp),
                    shape = RoundedCornerShape(15.dp),
                    elevation = 5.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = if (isChecked.value) color4 else Color.White)
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
            OutlinedButton(
                onClick = {
                    val navArg = EstimationScreenDestination.NavArgs(
                        id = null,
                        sectionNumber = sectionNumber,
                        treeNames = treeList.value.toTypedArray()
                    )
                    navigator.popBackStack()
                    navigator.navigate(EstimationScreenDestination(navArg))
                },
                enabled = treeList.value.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    disabledContentColor = Color.Gray,
                    backgroundColor = Color.White,
                    disabledBackgroundColor = Color.LightGray
                ),
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(
                    text = "Rozpocznij pracę",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                )
            }
        }
    }
}
