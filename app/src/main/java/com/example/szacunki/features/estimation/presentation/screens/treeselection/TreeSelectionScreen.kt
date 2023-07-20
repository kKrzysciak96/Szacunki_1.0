package com.example.szacunki.features.estimation.presentation.screens.treeselection


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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.szacunki.R
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
    sectionNumber: String, navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val treeList = rememberSaveable { mutableStateOf(listOf<String>()) }
    TreeSelectionScreen(
        sectionNumber = sectionNumber,
        context = context,
        treeList = treeList,
        navigateToEstimationScreen = navigator::navigateToEstimationScreen
    )
}

@Composable
private fun TreeSelectionScreen(
    sectionNumber: String,
    context: Context,
    treeList: MutableState<List<String>>,
    navigateToEstimationScreen: (String, Array<String>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brushList1.gradientBackground()
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Heading()
        PredefinedTreesTable(context = context, treeList = treeList)
        StartWorkButton(
            onClick = { navigateToEstimationScreen(sectionNumber, treeList.value.toTypedArray()) },
            enabled = treeList.value.isNotEmpty()
        )
    }
}

@Composable
fun Heading() {
    Text(
        text = stringResource(id = R.string.hint24),
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(bottom = 20.dp)
    )
}

@Composable
private fun PredefinedTreesTable(context: Context, treeList: MutableState<List<String>>) {
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
                Box(contentAlignment = Alignment.Center,
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
}

@Composable
private fun StartWorkButton(enabled: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = { onClick() },
        enabled = enabled, colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            disabledContentColor = Color.Gray,
            backgroundColor = Color.White,
            disabledBackgroundColor = Color.LightGray
        ), modifier = Modifier.padding(top = 20.dp)
    ) {
        Text(
            text = stringResource(id = R.string.hint25),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5,
        )
    }
}

private fun DestinationsNavigator.navigateToEstimationScreen(
    sectionNumber: String, treeList: Array<String>
) {
    popBackStack().also {
        navigate(
            EstimationScreenDestination(
                id = null,
                sectionNumber = sectionNumber,
                treeNames = treeList
            )
        )
    }
}
