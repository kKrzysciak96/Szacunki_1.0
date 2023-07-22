package com.eltescode.estimations.features.estimation.data.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eltescode.estimations.features.estimation.domain.model.EstimationDomain
import com.eltescode.estimations.features.estimation.domain.model.TreeDomain
import com.eltescode.estimations.features.estimation.domain.model.TreeQualityClassesDomain
import com.eltescode.estimations.features.estimation.domain.model.TreeRowDomain
import java.util.*


@Entity
data class EstimationCached(
    val trees: List<TreeCached>,
    val sectionNumber: String,
    val memo: String,
    val date: Date,
    @PrimaryKey
    val id: UUID
) {
    constructor(estimationDomain: EstimationDomain) : this(
        trees = estimationDomain.trees.map { TreeCached(it) },
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

data class TreeCached(
    val name: String,
    val treeRows: List<TreeRowCached>
) {
    constructor(treDomain: TreeDomain) : this(
        name = treDomain.name,
        treeRows = treDomain.treeRows.map { TreeRowCached(it) }
    )

    fun toTreeDomain() = TreeDomain(name = name, treeRows = treeRows.map { it.toTreeRowDomain() })
}

data class TreeRowCached(
    val diameter: String,
    @Embedded("TreeQualityClassesCached") val treeQualityClasses: TreeQualityClassesCached,
    val height: Int
) {
    constructor(treeRowDomain: TreeRowDomain) : this(
        diameter = treeRowDomain.diameter,
        treeQualityClasses = TreeQualityClassesCached(treeRowDomain.treeQualityClasses),
        height = treeRowDomain.height
    )

    fun toTreeRowDomain() = TreeRowDomain(
        diameter = diameter,
        treeQualityClasses = treeQualityClasses.toTreeQualityClassesDomain(),
        height = height
    )
}

data class TreeQualityClassesCached(
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
        classC = treeQualityClasses.classC,
    )

    fun toTreeQualityClassesDomain() = TreeQualityClassesDomain(
        class1 = class1,
        class2 = class2,
        class3 = class3,
        classA = classA,
        classB = classB,
        classC = classC,
    )
}