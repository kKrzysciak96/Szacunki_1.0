package com.example.szacunki.features.estimation.presentation.model

import com.example.szacunki.features.estimation.domain.model.EstimationDomain
import com.example.szacunki.features.estimation.domain.model.TreeDomain
import com.example.szacunki.features.estimation.domain.model.TreeQualityClassesDomain
import com.example.szacunki.features.estimation.domain.model.TreeRowDomain
import java.util.*

data class EstimationDisplayable(
    val trees: List<TreeDisplayable>,
    val sectionNumber: String,
    val memo: String,
    val date: Date,
    val id: Int
) {
    constructor(estimationDomain: EstimationDomain) : this(
        trees = estimationDomain.trees.map { TreeDisplayable(it) },
        sectionNumber = estimationDomain.sectionNumber,
        memo = estimationDomain.memo,
        date = estimationDomain.date,
        id = estimationDomain.id
    )

    fun toEstimationDomain() = EstimationDomain(
        trees = trees.map { it.toTreeDomain() },
        sectionNumber = sectionNumber,
        memo = memo,
        date = date,
        id = id
    )
}

data class TreeDisplayable(
    val name: String,
    val treeRows: List<TreeRowDisplayable>
) {
    constructor(treDomain: TreeDomain) : this(
        name = treDomain.name,
        treeRows = treDomain.treeRows.map { TreeRowDisplayable(it) }
    )

    fun toTreeDomain() = TreeDomain(name = name, treeRows = treeRows.map { it.toTreeRowDomain() })
}

data class TreeRowDisplayable(
    val diameter: String,
    val treeQualityClasses: TreeQualityClassesDisplayable,
    val height: Int,
) {
    constructor(treeRowDomain: TreeRowDomain) : this(
        diameter = treeRowDomain.diameter,
        treeQualityClasses = TreeQualityClassesDisplayable(treeRowDomain.treeQualityClasses),
        height = treeRowDomain.height
    )

    fun toTreeRowDomain() = TreeRowDomain(
        diameter = diameter,
        treeQualityClasses = treeQualityClasses.toTreeQualityClassesDomain(),
        height = height
    )
}

data class TreeQualityClassesDisplayable(
    val class1: Int,
    val class2: Int,
    val class3: Int,
    val classA: Int,
    val classB: Int,
    val classC: Int
) {
    constructor(treeQualityClasses: TreeQualityClassesDomain) : this(
        class1 = treeQualityClasses.class1,
        class2 = treeQualityClasses.class2,
        class3 = treeQualityClasses.class3,
        classA = treeQualityClasses.classA,
        classB = treeQualityClasses.classB,
        classC = treeQualityClasses.classC
    )

    fun toTreeQualityClassesDomain() = TreeQualityClassesDomain(
        class1 = class1,
        class2 = class2,
        class3 = class3,
        classA = classA,
        classB = classB,
        classC = classC
    )

}