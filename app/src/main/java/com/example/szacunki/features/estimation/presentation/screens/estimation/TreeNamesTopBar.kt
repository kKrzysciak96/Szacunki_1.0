package com.example.szacunki.features.estimation.presentation.screens.estimation

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.szacunki.R
import com.example.szacunki.core.calculations.color1
import com.example.szacunki.core.calculations.color2
import com.example.szacunki.core.calculations.createEstimationToRemoveTree
import com.example.szacunki.core.extensions.trimToDisplay
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.features.estimation.presentation.model.TreeDisplayable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TreeNamesTopBar(
    estimation: State<EstimationDisplayable>,
    viewModel: EstimationViewModel,
    treeIndexState: MutableState<Int>,
    treeNameState: MutableState<Boolean>,
    listState: LazyListState,
    scope: CoroutineScope

) {
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(color1),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            state = listState,
            flingBehavior = snapBehavior
        ) {
            itemsIndexed(estimation.value.trees) { itemIndex, item ->
                TreeNamesItem(
                    estimation = estimation,
                    viewModel = viewModel,
                    treeIndexState = treeIndexState,
                    itemIndex = itemIndex,
                    item = item,
                    listState = listState,
                    scope = scope
                )
            }
            item {
                IconAddTreesItem(treeNameState)
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TreeNamesItem(
    estimation: State<EstimationDisplayable>,
    viewModel: EstimationViewModel,
    treeIndexState: MutableState<Int>,
    itemIndex: Int,
    item: TreeDisplayable,
    listState: LazyListState,
    scope: CoroutineScope
) {

    val isContextTreeMenuVisible = rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .height(60.dp)
            .width(120.dp)
            .background(color = if (treeIndexState.value == itemIndex) color2 else color1)
            .combinedClickable(onClick = {
                if (itemIndex != 0) {
                    scope.launch { listState.scrollToItem(itemIndex - 1) }
                }
                treeIndexState.value = itemIndex
            }, onLongClick = {
                isContextTreeMenuVisible.value = true
            }), contentAlignment = Alignment.Center
    ) {
        ContextTreeMenu(
            viewModel = viewModel,
            estimation = estimation,
            item = item,
            itemIndex = itemIndex,
            treeIndexState = treeIndexState,
            isContextTreeMenuVisible
        ) { isContextTreeMenuVisible.value = false }
        val textToDisplay = item.name.trimToDisplay()
        Text(
            text = textToDisplay,
            style = MaterialTheme.typography.h6,
            modifier = Modifier

                .padding(
                    5.dp
                ), textAlign = TextAlign.Center
        )
        if (treeIndexState.value == itemIndex) {
            Card(
                backgroundColor = Color.White,
                shape = RoundedCornerShape(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.BottomCenter)
            ) {

            }
        }
    }
}

@Composable
fun ContextTreeMenu(
    viewModel: EstimationViewModel,
    estimation: State<EstimationDisplayable>,
    item: TreeDisplayable,
    itemIndex: Int,
    treeIndexState: MutableState<Int>,
    isContextTreeMenuVisible: MutableState<Boolean>,
    onDismiss: () -> Unit
) {
    DropdownMenu(expanded = isContextTreeMenuVisible.value, onDismissRequest = { onDismiss() }) {
        DropdownMenuItem(onClick = { }) {
            Text(text = "Edit")
        }
        DropdownMenuItem(onClick = {
            val maxIndex = estimation.value.trees.size - 1
            if (treeIndexState.value == maxIndex) {
                if (maxIndex != 0) {
                    treeIndexState.value--
                }
            }
            if (maxIndex != 0) {
                val newEstimation = createEstimationToRemoveTree(
                    estimation = estimation,
                    treeToRemove = item
                )
                viewModel.updateEstimationFlow(newEstimation)
            }
            onDismiss()
        }) {
            Text(text = "Delete")
        }
    }
}


@Composable
fun IconAddTreesItem(treeNameState: MutableState<Boolean>) {
    val context = LocalContext.current
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(50.dp)
            .width(70.dp)
    ) {
        Card(border = BorderStroke(2.dp, Color.Black), elevation = 4.dp) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_plus),
                contentDescription = null,
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp)
                    .background(color1)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                treeNameState.value = true
                            },
                            onTap = {
                                Toast
                                    .makeText(
                                        context,
                                        "Przytrzymaj aby dodać",
                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            })
                    }
            )
        }
    }
}

