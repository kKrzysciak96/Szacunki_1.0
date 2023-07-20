package com.example.szacunki.core.extensions

import android.content.Context
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.features.estimation.presentation.model.TreeDisplayable
import com.example.szacunki.features.estimation.presentation.model.TreeQualityClassesDisplayable
import com.example.szacunki.features.estimation.presentation.model.TreeRowDisplayable
import com.example.szacunki.features.estimation.presentation.screens.estimation.ButtonId

fun EstimationDisplayable.createEstimationToUpdateQuantities(
    newQuantity: Int, treeIndex: Int, diameterIndex: Int, buttonId: ButtonId
): EstimationDisplayable {
    val newTreeQualityClassesDisplayable: TreeQualityClassesDisplayable =
        updateTreeQualityClassesDisplayable(
            treeIndex = treeIndex,
            diameterIndex = diameterIndex,
            buttonId = buttonId,
            newQuantity = newQuantity
        )
    val newTreeRowDisplayable: TreeRowDisplayable = updateTreeRowDisplayableTreeQualityClasses(
        treeIndex = treeIndex,
        diameterIndex = diameterIndex,
        newTreeQualityClassesDisplayable = newTreeQualityClassesDisplayable
    )
    val newTreeDisplayable: TreeDisplayable = updateTreeDisplayableTreeRows(
        treeIndex = treeIndex,
        diameterIndex = diameterIndex,
        newTreeRowDisplayable = newTreeRowDisplayable
    )
    val newTreeDisplayableList: MutableList<TreeDisplayable> = this.trees.toMutableList()
    newTreeDisplayableList[treeIndex] = newTreeDisplayable
    return this.copy(trees = newTreeDisplayableList)
}

fun EstimationDisplayable.createEstimationToRemoveTree(
    treeToRemove: TreeDisplayable
): EstimationDisplayable {
    val newTreeDisplayableList: MutableList<TreeDisplayable> = this.trees.toMutableList()
    newTreeDisplayableList.remove(treeToRemove)
    return this.copy(trees = newTreeDisplayableList)
}

fun EstimationDisplayable.createEstimationToUpdateTreeHeight(
    treeIndex: Int, diameterIndex: Int, height: Int
): EstimationDisplayable {
    val newTreeRowDisplayable: TreeRowDisplayable =
        this.trees[treeIndex].treeRows[diameterIndex].copy(height = height)
    val newTreeRowList: MutableList<TreeRowDisplayable> =
        this.trees[treeIndex].treeRows.toMutableList()
    newTreeRowList[diameterIndex] = newTreeRowDisplayable
    val newTreeDisplayable: TreeDisplayable = this.trees[treeIndex].copy(treeRows = newTreeRowList)
    val newTreeDisplayableList: MutableList<TreeDisplayable> = this.trees.toMutableList()
    newTreeDisplayableList[treeIndex] = newTreeDisplayable
    return this.copy(trees = newTreeDisplayableList)
}

fun EstimationDisplayable.createEstimationToUpdateTreeNames(
    context: Context,
    treeName: String
): EstimationDisplayable {
    val newTreeDisplayable = TreeDisplayable(
        name = treeName,
        treeRows = context.getBaseDiameterList().map { TreeRowDisplayable(diameter = it) })
    val newTreeDisplayableList: MutableList<TreeDisplayable> = this.trees.toMutableList()
    newTreeDisplayableList.add(newTreeDisplayable)
    return this.copy(trees = newTreeDisplayableList)
}

fun EstimationDisplayable.createEstimationToUpdateMemo(
    memo: String
): EstimationDisplayable {
    return this.copy(memo = memo)
}

fun EstimationDisplayable.updateTreeDisplayableTreeRows(
    treeIndex: Int, diameterIndex: Int, newTreeRowDisplayable: TreeRowDisplayable
): TreeDisplayable {
    val newTreeRowList: MutableList<TreeRowDisplayable> =
        this.trees[treeIndex].treeRows.toMutableList()
    newTreeRowList[diameterIndex] = newTreeRowDisplayable
    return this.trees[treeIndex].copy(treeRows = newTreeRowList)
}

fun EstimationDisplayable.updateTreeQualityClassesDisplayable(
    treeIndex: Int, diameterIndex: Int, newQuantity: Int, buttonId: ButtonId
): TreeQualityClassesDisplayable {
    return with(this.trees[treeIndex].treeRows[diameterIndex].treeQualityClasses) {
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

fun EstimationDisplayable.updateTreeRowDisplayableTreeQualityClasses(
    treeIndex: Int,
    diameterIndex: Int,
    newTreeQualityClassesDisplayable: TreeQualityClassesDisplayable
): TreeRowDisplayable {
    return this.trees[treeIndex].treeRows[diameterIndex].copy(treeQualityClasses = newTreeQualityClassesDisplayable)
}

fun EstimationDisplayable.performSubtraction(
    treeIndex: Int, diameterIndex: Int, buttonId: ButtonId
): Int {
    with(this.trees[treeIndex].treeRows[diameterIndex].treeQualityClasses) {
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

fun EstimationDisplayable.performAddition(
    treeIndex: Int, diameterIndex: Int, buttonId: ButtonId
): Int {
    with(this.trees[treeIndex].treeRows[diameterIndex].treeQualityClasses) {
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

fun EstimationDisplayable.createPdfFileName() =
    this.sectionNumber + "_" + this.date.toLocalDateTime().prepareDateToSave() + ".pdf"

