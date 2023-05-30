package com.example.szacunki.features.estimation.presentation.screens.estimation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.szacunki.R
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.features.estimation.presentation.model.TreeDisplayable
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel


@Destination
@Composable
fun EstimationScreen(id: Int?) {

    val viewModel = getViewModel<EstimationViewModel>()
    if (id == null) {
        viewModel.createNewSheet()
    } else {
        viewModel.onIdPassed(id)
    }
    val estimation = viewModel.estimationFlow.collectAsState()
    val index = remember { mutableStateOf(0) }

    estimation.value?.let { estimationDisplayable ->
        Scaffold(
            topBar = { TreeNamesTopBar(estimationDisplayable.trees, index) },
            bottomBar = { BottomInfoBar2(estimationDisplayable) }

        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TitleParametersRow()
                LazyColumnParameters(estimationDisplayable.trees, index.value)
            }
            it
        }
    }
}

@Composable
fun TreeNamesTopBar(treeList: List<TreeDisplayable>, index: MutableState<Int>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(treeList) { itemIndex, item ->
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .background(color = if (index.value == itemIndex) color2 else color1)
                    .clickable {
                        index.value = itemIndex
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(
                        top = 5.dp,
                        bottom = 5.dp,
                        start = 10.dp,
                        end = 10.dp
                    )
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .background(color = color1), contentAlignment = Alignment.Center
            ) {
                OutlinedButton(onClick = {

                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_plus),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun TitleParametersRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color2),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Średnica")
        Text(text = "Liczba Drzew")
        Text(text = "Wysokość")
    }
}

@Composable
fun LazyColumnParameters(treeList: List<TreeDisplayable>, index: Int) {
    LazyColumn(
        content = {
            itemsIndexed(treeList[index].treeRows) { itemIndex, item ->
                TreeParametersRow(
                    diameter = item.diameter,
                    classQuantity = item.treeQualityClasses.class3,
                    height = item.height,
                    treeList = treeList,
                    treeIndex = index,
                    diameterIndex = itemIndex
                )
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp)
    )
}


@Composable
fun TreeParametersRow(
    diameter: String,
    classQuantity: Int,
    height: Int,
    treeList: List<TreeDisplayable>,
    treeIndex: Int,
    diameterIndex: Int
) {
    val classesQuantity = remember {
        mutableStateOf(treeList[treeIndex].treeRows[diameterIndex].treeQualityClasses.class3)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = diameter,
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1
        )
        Button(onClick = { classesQuantity.value-- }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_minus),
                contentDescription = null
            )
        }

        Text(text = classesQuantity.value.toString())
        Button(onClick = { classesQuantity.value++ }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_plus),
                contentDescription = null
            )
        }
        Text(text = height.toString())

    }
}

@Composable
fun BottomInfoBar(estimation: EstimationDisplayable) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(bottom = 50.dp)
            .background(color = color2), contentAlignment = Alignment.Center
    ) {
        Text(text = estimation.date.time.toString(), style = MaterialTheme.typography.h6)
    }
}

@Composable
fun BottomInfoBar2(estimation: EstimationDisplayable) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        backgroundColor = color2
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = color2), contentAlignment = Alignment.Center
        ) {
            Text(text = estimation.date.time.toString(), style = MaterialTheme.typography.h6)
        }
    }
}

val color1 = Color(0xff1e966e)
val color2 = Color(0xff04704c)
val color3 = Color(0xff2debab)
val color4 = Color.White



