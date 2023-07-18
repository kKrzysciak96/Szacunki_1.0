package com.example.szacunki.features.estimation.presentation.screens.estimation

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.szacunki.R
import com.example.szacunki.core.extensions.createEstimationToRemoveTree
import com.example.szacunki.core.extensions.showLongHint
import com.example.szacunki.core.extensions.trimToDisplay
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.features.estimation.presentation.model.TreeDisplayable
import com.example.szacunki.ui.theme.color1
import com.example.szacunki.ui.theme.color2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TreeNamesTopBar(
    estimation: EstimationDisplayable,
    updateEstimation: (EstimationDisplayable) -> Unit,
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
            itemsIndexed(estimation.trees) { itemIndex, item ->
                TreeNamesItem(
                    estimation = estimation,
                    updateEstimation = updateEstimation,
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
    estimation: EstimationDisplayable,
    updateEstimation: (EstimationDisplayable) -> Unit,
    treeIndexState: MutableState<Int>,
    itemIndex: Int,
    item: TreeDisplayable,
    listState: LazyListState,
    scope: CoroutineScope
) {
    val isContextTreeMenuVisible = rememberSaveable { mutableStateOf(false) }
    val textToDisplay = item.name.trimToDisplay()
    Box(
        modifier = Modifier
            .height(60.dp)
            .width(120.dp)
            .background(color = if (treeIndexState.value == itemIndex) color2 else color1)
            .combinedClickable(
                onClick = {
                    if (itemIndex != 0) {
                        scope.launch { listState.scrollToItem(itemIndex - 1) }
                    }
                    treeIndexState.value = itemIndex
                },
                onLongClick = { isContextTreeMenuVisible.value = true }),
        contentAlignment = Alignment.Center
    ) {
        ContextTreeMenu(
            updateEstimation = updateEstimation,
            estimation = estimation,
            item = item,
            treeIndexState = treeIndexState,
            isContextTreeMenuVisible = isContextTreeMenuVisible,
            onDismiss = { isContextTreeMenuVisible.value = false }
        )
        Text(
            text = textToDisplay,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(5.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ContextTreeMenu(
    updateEstimation: (EstimationDisplayable) -> Unit,
    estimation: EstimationDisplayable,
    item: TreeDisplayable,
    treeIndexState: MutableState<Int>,
    isContextTreeMenuVisible: MutableState<Boolean>,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = isContextTreeMenuVisible.value,
        onDismissRequest = { onDismiss() }) {
        DropdownMenuItem(onClick = {
            val maxIndex = estimation.trees.size - 1
            if (treeIndexState.value == maxIndex && maxIndex != 0) {
                treeIndexState.value--
            }
            if (maxIndex != 0) {
                val newEstimation = estimation.createEstimationToRemoveTree(item)
                updateEstimation(newEstimation)
            }
            onDismiss()
        }) {
            Text(text = stringResource(id = R.string.hint14))
        }
    }
}

@Composable
private fun IconAddTreesItem(treeNameState: MutableState<Boolean>) {
    val context = LocalContext.current
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(50.dp)
            .width(70.dp)
    ) {
        Card(
            border = BorderStroke(2.dp, Color.Black),
            elevation = 4.dp
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_plus),
                contentDescription = null,
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp)
                    .background(color1)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { treeNameState.value = true },
                            onTap = { context.showLongHint(R.string.hint15) })
                    })
        }
    }
}

