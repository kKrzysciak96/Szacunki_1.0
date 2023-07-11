package com.example.szacunki.core.calculations

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.features.estimation.presentation.model.TreeDisplayable
import com.example.szacunki.features.estimation.presentation.model.TreeQualityClassesDisplayable
import com.example.szacunki.features.estimation.presentation.model.TreeRowDisplayable
import com.example.szacunki.features.estimation.presentation.screens.estimation.ButtonId

fun createEstimationToUpdateQuantities(
    newQuantity: Int,
    estimation: State<EstimationDisplayable>,
    treeIndex: Int,
    diameterIndex: Int,
    buttonId: ButtonId
): EstimationDisplayable {
    val newTreeQualityClassesDisplayable: TreeQualityClassesDisplayable =
        updateTreeQualityClassesDisplayable(
            estimation = estimation,
            treeIndex = treeIndex,
            diameterIndex = diameterIndex,
            buttonId = buttonId,
            newQuantity = newQuantity
        )
    val newTreeRowDisplayable: TreeRowDisplayable = updateTreeRowDisplayableTreeQualityClasses(
        estimation = estimation,
        treeIndex = treeIndex,
        diameterIndex = diameterIndex,
        newTreeQualityClassesDisplayable = newTreeQualityClassesDisplayable
    )
    val newTreeDisplayable: TreeDisplayable = updateTreeDisplayableTreeRows(
        estimation = estimation,
        treeIndex = treeIndex,
        diameterIndex = diameterIndex,
        newTreeRowDisplayable = newTreeRowDisplayable
    )

    val newTreeDisplayableList: MutableList<TreeDisplayable> =
        estimation.value.trees.toMutableList()
    newTreeDisplayableList[treeIndex] = newTreeDisplayable

    return estimation.value.copy(trees = newTreeDisplayableList)
}

fun createEstimationToRemoveTree(
    estimation: State<EstimationDisplayable>, treeToRemove: TreeDisplayable
): EstimationDisplayable {
    val newTreeDisplayableList: MutableList<TreeDisplayable> =
        estimation.value.trees.toMutableList()
    newTreeDisplayableList.remove(treeToRemove)
    return estimation.value.copy(trees = newTreeDisplayableList)
}

fun createEstimationToUpdateTreeHeight(
    estimation: State<EstimationDisplayable>, treeIndex: Int, diameterIndex: Int, height: Int
): EstimationDisplayable {

    val newTreeRowDisplayable: TreeRowDisplayable =
        estimation.value.trees[treeIndex].treeRows[diameterIndex].copy(height = height)
    val newTreeRowList: MutableList<TreeRowDisplayable> =
        estimation.value.trees[treeIndex].treeRows.toMutableList()
    newTreeRowList[diameterIndex] = newTreeRowDisplayable
    val newTreeDisplayable: TreeDisplayable =
        estimation.value.trees[treeIndex].copy(treeRows = newTreeRowList)
    val newTreeDisplayableList: MutableList<TreeDisplayable> =
        estimation.value.trees.toMutableList()
    newTreeDisplayableList[treeIndex] = newTreeDisplayable

    return estimation.value.copy(trees = newTreeDisplayableList)
}

fun createEstimationToUpdateTreeNames(
    estimation: State<EstimationDisplayable>, treeName: String, removeTree: Boolean = false
): EstimationDisplayable {

    val newTreeDisplayable: TreeDisplayable = TreeDisplayable(name = treeName)
    val newTreeDisplayableList: MutableList<TreeDisplayable> =
        estimation.value.trees.toMutableList()

    newTreeDisplayableList.add(newTreeDisplayable)

    return estimation.value.copy(trees = newTreeDisplayableList)
}


fun performSubtraction(
    estimation: State<EstimationDisplayable>, treeIndex: Int, diameterIndex: Int, buttonId: ButtonId

): Int {
    with(estimation.value.trees[treeIndex].treeRows[diameterIndex].treeQualityClasses) {
        return when (buttonId) {
            ButtonId.CLASS1 -> class1.let { if (it != 0) it - 1 else 0 }
            ButtonId.CLASS2 -> class2.let { if (it != 0) it - 1 else 0 }
            ButtonId.CLASS3 -> class3.let { if (it != 0) it - 1 else 0 }
            ButtonId.CLASSA -> classA.let { if (it != 0) it - 1 else 0 }
            ButtonId.CLASSB -> classB.let { if (it != 0) it - 1 else 0 }
            ButtonId.CLASSC -> classC.let { if (it != 0) it - 1 else 0 }
        }
    }
}

fun performAddition(
    estimation: State<EstimationDisplayable>, treeIndex: Int, diameterIndex: Int, buttonId: ButtonId

): Int {
    with(estimation.value.trees[treeIndex].treeRows[diameterIndex].treeQualityClasses) {
        return when (buttonId) {
            ButtonId.CLASS1 -> class1 + 1
            ButtonId.CLASS2 -> class2 + 1
            ButtonId.CLASS3 -> class3 + 1
            ButtonId.CLASSA -> classA + 1
            ButtonId.CLASSB -> classB + 1
            ButtonId.CLASSC -> classC + 1
        }
    }
}

val color1 = Color(0xff1e966e)
val color2 = Color(0xff04704c)
val color3 = Color(0xff2debab)
val color4 = Color(0xff70c5ad)


fun createNewTreeDisplayable(treeName: String): TreeDisplayable {
    return TreeDisplayable(name = treeName)
}

fun updateTreeDisplayableTreeRows(
    estimation: State<EstimationDisplayable>,
    treeIndex: Int,
    diameterIndex: Int,
    newTreeRowDisplayable: TreeRowDisplayable
): TreeDisplayable {
    val newTreeRowList: MutableList<TreeRowDisplayable> =
        estimation.value.trees[treeIndex].treeRows.toMutableList()
    newTreeRowList[diameterIndex] = newTreeRowDisplayable
    return estimation.value.trees[treeIndex].copy(treeRows = newTreeRowList)
}


fun updateTreeQualityClassesDisplayable(
    estimation: State<EstimationDisplayable>,
    treeIndex: Int,
    diameterIndex: Int,
    newQuantity: Int,
    buttonId: ButtonId
): TreeQualityClassesDisplayable {
    return with(estimation.value.trees[treeIndex].treeRows[diameterIndex].treeQualityClasses) {
        when (buttonId) {
            ButtonId.CLASS1 -> copy(class1 = newQuantity)
            ButtonId.CLASS2 -> copy(class2 = newQuantity)
            ButtonId.CLASS3 -> copy(class3 = newQuantity)
            ButtonId.CLASSA -> copy(classA = newQuantity)
            ButtonId.CLASSB -> copy(classB = newQuantity)
            ButtonId.CLASSC -> copy(classC = newQuantity)
        }
    }
}

fun updateTreeRowDisplayableTreeQualityClasses(
    estimation: State<EstimationDisplayable>,
    treeIndex: Int,
    diameterIndex: Int,
    newTreeQualityClassesDisplayable: TreeQualityClassesDisplayable
): TreeRowDisplayable {
    return estimation.value.trees[treeIndex].treeRows[diameterIndex].copy(treeQualityClasses = newTreeQualityClassesDisplayable)
}

fun updateEstimationMemo(
    estimation: State<EstimationDisplayable>,
    memo: String
): EstimationDisplayable {
    return estimation.value.copy(memo = memo)
}



