package com.eltescode.estimations.features.estimation.presentation.model

import com.eltescode.estimations.features.estimation.domain.model.EstimationDomain
import com.eltescode.estimations.features.estimation.domain.model.TreeDomain
import com.eltescode.estimations.features.estimation.domain.model.TreeQualityClassesDomain
import com.eltescode.estimations.features.estimation.domain.model.TreeRowDomain
import java.util.*

data class EstimationDisplayable(
    val trees: List<TreeDisplayable>,
    val sectionNumber: String,
    val memo: String = "",
    val date: Date = Date(),
    val id: UUID = UUID.randomUUID()
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
    val treeQualityClasses: TreeQualityClassesDisplayable = TreeQualityClassesDisplayable(),
    val height: Int = 0,
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
    val class1: Int = 0,
    val class2: Int = 0,
    val class3: Int = 0,
    val classA: Int = 0,
    val classB: Int = 0,
    val classC: Int = 0
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



