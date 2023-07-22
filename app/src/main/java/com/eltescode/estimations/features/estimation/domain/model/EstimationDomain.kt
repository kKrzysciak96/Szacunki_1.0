package com.eltescode.estimations.features.estimation.domain.model

import java.util.*

data class EstimationDomain(
    val trees: List<TreeDomain>,
    val sectionNumber: String,
    val memo: String,
    val date: Date,
    val id: UUID
)

data class TreeDomain(
    val name: String,
    val treeRows: List<TreeRowDomain>
)

data class TreeRowDomain(
    val diameter: String,
    val treeQualityClasses: TreeQualityClassesDomain,
    val height: Int
)

data class TreeQualityClassesDomain(
    val class1: Int,
    val class2: Int,
    val class3: Int,
    val classA: Int,
    val classB: Int,
    val classC: Int
)



